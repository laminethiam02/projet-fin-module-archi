import { ApplicationConfig, APP_INITIALIZER, importProvidersFrom } from '@angular/core';
import { provideRouter } from '@angular/router';
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { KeycloakAngularModule, KeycloakService } from 'keycloak-angular';
import { routes } from './app.routes';
import { keycloakConfig, keycloakInitOptions } from './auth/keycloak.config';
import { authInterceptor } from './auth/auth.interceptor';


function initializeKeycloak(keycloak: KeycloakService) {
  return () =>
    keycloak.init({
      config: keycloakConfig,
      initOptions: {
        ...keycloakInitOptions,
        checkLoginIframe: false
      }
    }).catch(err => {
      console.error('Keycloak initialization failed', err);
    });
}

export const appConfig: ApplicationConfig = {
  providers: [
    // Configuration du routeur
    provideRouter(routes),

    // Configuration HTTP avec intercepteur
    provideHttpClient(
      withInterceptors([authInterceptor])
    ),

    // Intégration Keycloak
    importProvidersFrom(KeycloakAngularModule),
    {
      provide: APP_INITIALIZER,
      useFactory: initializeKeycloak,
      multi: true,
      deps: [KeycloakService]
    },
    KeycloakService
  ]
};
