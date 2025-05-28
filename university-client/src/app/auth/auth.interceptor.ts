import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const keycloak = inject(KeycloakService);

  if (keycloak.getKeycloakInstance().token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${keycloak.getKeycloakInstance().token}`
      }
    });
    return next(authReq);
  }

  return next(req);
};
