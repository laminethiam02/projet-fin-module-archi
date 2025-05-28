import { TestBed } from '@angular/core/testing';

import { AuthService } from './keycloak-auth.service';

describe('KeycloakAuthService', () => {
  let service: AuthService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
