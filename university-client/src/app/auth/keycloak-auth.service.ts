import { Injectable } from '@angular/core';
import { KeycloakService } from 'keycloak-angular';
import { KeycloakProfile, KeycloakTokenParsed } from 'keycloak-js';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private userProfile = new BehaviorSubject<any>(null);
  currentUser$ = this.userProfile.asObservable();

  constructor(private keycloakService: KeycloakService) {
    this.loadUser();
  }

  public getLoggedUser(): KeycloakTokenParsed | undefined {
    try {
      return this.keycloakService.getKeycloakInstance().idTokenParsed;
    } catch (e) {
      console.error('Exception', e);
      return undefined;
    }
  }

  public isLoggedIn(): Promise<boolean> {
    return this.keycloakService.isLoggedIn();
  }

  public loadUserProfile(): Promise<KeycloakProfile> {
    return this.keycloakService.loadUserProfile();
  }

  public login(): Promise<void> {
    return this.keycloakService.login();
  }

  public logout(): Promise<void> {
    return this.keycloakService.logout();
  }

  public redirectToProfile(): Promise<void> {
    return this.keycloakService.getKeycloakInstance().accountManagement();
  }

  public getRoles(): string[] {
    return this.keycloakService.getUserRoles();
  }

  public isAdmin(): boolean {
    return this.getRoles().includes('ROLE ADMIN');
  }

  public isTeacher(): boolean {
    return this.getRoles().includes('ROLE ENSEIGNANT');
  }

  public isStudent(): boolean {
    return this.getRoles().includes('ROLE ETUDIANT');
  }

  public getToken(): Promise<string> {
    return this.keycloakService.getToken();
  }

  private async loadUser() {
    if (await this.isLoggedIn()) {
      try {
        const profile = await this.loadUserProfile();
        const roles = this.getRoles();
        this.userProfile.next({
          ...profile,
          roles,
          username: profile.username,
          firstName: profile.firstName,
          lastName: profile.lastName,
          email: profile.email
        });
      } catch (error) {
        console.error('Failed to load user profile', error);
      }
    }
  }
}
