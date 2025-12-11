import http from 'k6/http';
import { check, sleep } from 'k6';
import { Rate } from 'k6/metrics';

// Configuración
const BASE_URL = __ENV.BASE_URL || 'http://localhost:8080/api/1/compra-tu-auto';
const ADMIN_EMAIL = __ENV.ADMIN_EMAIL || 'admin@compraauto.com';
const ADMIN_PASSWORD = __ENV.ADMIN_PASSWORD || 'admin123';
const VUS = parseInt(__ENV.VUS || '1'); // Virtual Users (1 para crear secuencialmente)
const ITERATIONS = parseInt(__ENV.ITERATIONS || '10'); // Número de usuarios a crear

// Métricas personalizadas
const errorRate = new Rate('errors');

export const options = {
  stages: [
    { duration: '1s', target: VUS }, // Ramp up
    { duration: '10s', target: VUS }, // Mantener
    { duration: '1s', target: 0 },    // Ramp down
  ],
  thresholds: {
    'http_req_duration': ['p(95)<2000'], // 95% de las requests deben ser < 2s
    'errors': ['rate<0.1'], // Menos del 10% de errores
  },
};

export default function () {
  // Paso 1: Login como ADMIN para obtener token
  const loginPayload = JSON.stringify({
    usuario: ADMIN_EMAIL,
    password: ADMIN_PASSWORD
  });

  const loginParams = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const loginRes = http.post(`${BASE_URL}/auth/login`, loginPayload, loginParams);
  
  const loginSuccess = check(loginRes, {
    'login status 200': (r) => r.status === 200,
    'login has token': (r) => {
      if (r.status === 200) {
        try {
          const body = JSON.parse(r.body);
          return body.token !== undefined && body.token !== null;
        } catch (e) {
          return false;
        }
      }
      return false;
    },
  });

  if (!loginSuccess) {
    errorRate.add(1);
    console.error(`Login failed: ${loginRes.status} - ${loginRes.body}`);
    return;
  }

  let loginBody;
  try {
    loginBody = JSON.parse(loginRes.body);
  } catch (e) {
    errorRate.add(1);
    console.error(`Failed to parse login response: ${e}`);
    return;
  }

  const adminToken = loginBody.token;

  // Headers con autenticación para las siguientes requests
  const authHeaders = {
    'Content-Type': 'application/json',
    'Authorization': `Bearer ${adminToken}`,
  };

  // Paso 2: Crear usuarios y concesionarias
  for (let i = 0; i < ITERATIONS; i++) {
    // Crear usuario CONCESIONARIA
    const usuarioPayload = JSON.stringify({
      email: `user_${i}@compraauto.com`,
      password: `uservendedor${i}`,
      nombre: 'user',
      apellido: `vendedor ${i}`,
      tipoUsuario: 'CONCESIONARIA'
    });

    const usuarioRes = http.post(`${BASE_URL}/usuarios`, usuarioPayload, {
      headers: {
        'Content-Type': 'application/json',
      },
    });

    const usuarioSuccess = check(usuarioRes, {
      'usuario created status 201': (r) => r.status === 201,
      'usuario has id': (r) => {
        if (r.status === 201) {
          try {
            const body = JSON.parse(r.body);
            return body.id !== undefined && body.id !== null;
          } catch (e) {
            return false;
          }
        }
        return false;
      },
    });

    if (!usuarioSuccess) {
      errorRate.add(1);
      console.error(`Failed to create usuario ${i}: ${usuarioRes.status} - ${usuarioRes.body}`);
      continue; // Continuar con el siguiente aunque falle este
    }

    let usuarioBody;
    try {
      usuarioBody = JSON.parse(usuarioRes.body);
    } catch (e) {
      errorRate.add(1);
      console.error(`Failed to parse usuario response ${i}: ${e}`);
      continue;
    }

    const usuarioId = usuarioBody.id;

    // Pequeña pausa entre requests
    sleep(0.5);

    // Crear concesionaria asociada al usuario
    const concesionariaPayload = JSON.stringify({
      nombre: `Concesionaria_${i}`,
      cuit: `20-12345678-${i}`,
      usuarioId: usuarioId
    });

    const concesionariaRes = http.post(
      `${BASE_URL}/concesionarias`,
      concesionariaPayload,
      {
        headers: authHeaders, // Requiere token de ADMIN
      }
    );

    const concesionariaSuccess = check(concesionariaRes, {
      'concesionaria created status 201': (r) => r.status === 201,
      'concesionaria has id': (r) => {
        if (r.status === 201) {
          try {
            const body = JSON.parse(r.body);
            return body.id !== undefined && body.id !== null;
          } catch (e) {
            return false;
          }
        }
        return false;
      },
    });

    if (!concesionariaSuccess) {
      errorRate.add(1);
      console.error(`Failed to create concesionaria ${i}: ${concesionariaRes.status} - ${concesionariaRes.body}`);
    } else {
      console.log(`✓ Created usuario ${i} (ID: ${usuarioId}) and Concesionaria_${i}`);
    }

    // Pausa entre iteraciones
    sleep(1);
  }
}

export function handleSummary(data) {
  const summary = `
  ====================
  Test Summary
  ====================
  Total Requests: ${data.metrics.http_reqs.values.count}
  Failed Requests: ${(data.metrics.http_req_failed.values.rate * 100).toFixed(2)}%
  Avg Response Time: ${data.metrics.http_req_duration.values.avg.toFixed(2)}ms
  Min Response Time: ${data.metrics.http_req_duration.values.min.toFixed(2)}ms
  Max Response Time: ${data.metrics.http_req_duration.values.max.toFixed(2)}ms
  P95 Response Time: ${data.metrics.http_req_duration.values['p(95)'].toFixed(2)}ms
  P99 Response Time: ${data.metrics.http_req_duration.values['p(99)'].toFixed(2)}ms
  ====================
  `;
  return {
    'stdout': summary,
  };
}

