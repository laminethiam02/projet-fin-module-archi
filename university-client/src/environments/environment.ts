export const environment = {
  production: false,

  // Configuration JHipster Gateway
  gatewayUrl: 'http://localhost:8080',

  // Configuration Keycloak
  keycloak: {
    url: 'http://localhost:9080/auth', // URL de base Keycloak
    realm: 'university-realm',         // Nom du realm
    clientId: 'university-client',     // Client ID
    credentials: {
      secret: 'votre-client-secret'    // À remplacer par votre secret
    }
  },

  // Configuration des microservices
  services: {
    user: {
      basePath: 'userservice',
      endpoints: {
        profile: 'profile',
        roles: 'roles'
      }
    },
    student: {
      basePath: 'studentservice',
      endpoints: {
        records: 'students',
        academicHistory: 'academic-history'
      }
    },
    teacher: {
      basePath: 'teacherservice',
      endpoints: {
        courses: 'teachers',
        assignments: 'assignments'
      }
    },
    note: {
      basePath: 'noteservice',
      endpoints: {
        grades: 'grades',
        evaluations: 'evaluations'
      }
    },
    reporting: {
      basePath: 'reportingservice',
      endpoints: {
        stats: 'stats',
        exports: 'exports'
      }
    }
  },

  // Paramètres d'application
  app: {
    defaultPageSize: 20,
    enableDebug: true,
    version: '1.0.0'
  },

  // Configuration des logs
  logging: {
    level: 'debug',
    serverLogLevel: 'info',
    serverLoggingUrl: 'http://localhost:8080/api/logs'
  },

  // URLs importantes
  urls: {
    help: 'http://localhost:8080/help',
    apiDocs: 'http://localhost:8080/v2/api-docs'
  }
};
