import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule],
  template: `
    <nav>
      <a routerLink="">Home</a> |
      <a routerLink="admin">Admin</a> |
      <a routerLink="teacher">Teacher</a> |
      <a routerLink="student">Student</a>
    </nav>
    <router-outlet></router-outlet>
  `
})
export class AppComponent {}
