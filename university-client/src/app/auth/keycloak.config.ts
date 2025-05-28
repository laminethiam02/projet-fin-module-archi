import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakInitOptions } from 'keycloak-js';
export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloakService = inject(KeycloakService);

  // Ne pas intercepter les requêtes vers Keycloak ou les assets
  if (req.url.includes('keycloak') || req.url.includes('/assets/')) {
    return next(req);
  }

  const token = keycloakService.getKeycloakInstance().token;

  if (token) {
    const authReq = req.clone({
      headers: req.headers.set('Authorization', `Bearer ${token}`)
    });
    return next(authReq);
  }

  return next(req);
};export const keycloakConfig = {
  url: 'http://localhost:9080/auth',
  realm: 'university-realm',
  clientId: 'university-frontend',
  redirectUri: 'http://localhost:4200/'
};
export const keycloakInitOptions: KeycloakInitOptions = {
  onLoad: 'login-required',
  checkLoginIframe: false,
  redirectUri: 'http://localhost:4200/',
  silentCheckSsoRedirectUri: 'http://localhost:4200/assets/silent-check-sso.html'
};
