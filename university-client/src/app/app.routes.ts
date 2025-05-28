import { Routes } from '@angular/router';
import { HomeComponent } from './home/home.component';

export const routes: Routes = [
  { path: '', component: HomeComponent },
  {
    path: 'admin',
    loadComponent: () => import('./admin/admin.component').then(m => m.AdminComponent)
  },
  {
    path: 'teacher',
    loadComponent: () => import('./teacher/teacher.component').then(m => m.TeacherComponent)
  },
  {
    path: 'student',
    loadComponent: () => import('./student/student.component').then(m => m.StudentComponent)
  }
];
