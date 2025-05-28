import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../environments/environment';
import { Observable } from 'rxjs';
import { Note, Student, Teacher, Course, UserProfile } from '../models';

@Injectable({ providedIn: 'root' })
export class ApiService {
  private readonly GATEWAY_PREFIX = `${environment.gatewayUrl}`;

  constructor(private http: HttpClient) {}

  // ==================== UserService ====================
  getCurrentUserProfile(): Observable<UserProfile> {
    return this.http.get<UserProfile>(`${this.GATEWAY_PREFIX}/userservice/api/profile`);
  }

  updateUserProfile(profile: Partial<UserProfile>): Observable<UserProfile> {
    return this.http.put<UserProfile>(`${this.GATEWAY_PREFIX}/userservice/api/profile`, profile);
  }

  // ==================== StudentService ====================
  getStudentDetails(studentId: string): Observable<Student> {
    return this.http.get<Student>(`${this.GATEWAY_PREFIX}/studentservice/api/students/${studentId}`);
  }

  getAcademicHistory(studentId: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.GATEWAY_PREFIX}/studentservice/api/students/${studentId}/history`);
  }

  // ==================== TeacherService ====================
  getTeacherCourses(teacherId: string): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.GATEWAY_PREFIX}/teacherservice/api/teachers/${teacherId}/courses`);
  }

  assignCourseToTeacher(teacherId: string, courseId: string): Observable<void> {
    return this.http.post<void>(
      `${this.GATEWAY_PREFIX}/teacherservice/api/teachers/${teacherId}/courses/${courseId}`,
      {}
    );
  }

  // ==================== NotesService ====================
  submitGrade(gradeData: {
    studentId: string;
    courseId: string;
    value: number;
    type: 'EXAM' | 'ASSIGNMENT';
  }): Observable<Note> {
    return this.http.post<Note>(`${this.GATEWAY_PREFIX}/noteservice/api/grades`, gradeData);
  }

  getStudentGrades(studentId: string): Observable<Note[]> {
    return this.http.get<Note[]>(`${this.GATEWAY_PREFIX}/noteservice/api/students/${studentId}/grades`);
  }

  // ==================== ReportingService ====================
  getSuccessRates(courseId?: string): Observable<any> {
    const url = courseId
      ? `${this.GATEWAY_PREFIX}/reportingservice/api/stats/success?courseId=${courseId}`
      : `${this.GATEWAY_PREFIX}/reportingservice/api/stats/success`;
    return this.http.get<any>(url);
  }

  // ==================== Generic Methods ====================
  private buildUrl(service: string, endpoint: string): string {
    return `${this.GATEWAY_PREFIX}/${service}/api/${endpoint}`;
  }

  get<T>(service: string, endpoint: string, params?: any): Observable<T> {
    return this.http.get<T>(this.buildUrl(service, endpoint), { params });
  }

  post<T>(service: string, endpoint: string, body: any): Observable<T> {
    return this.http.post<T>(this.buildUrl(service, endpoint), body);
  }

  put<T>(service: string, endpoint: string, body: any): Observable<T> {
    return this.http.put<T>(this.buildUrl(service, endpoint), body);
  }

  delete<T>(service: string, endpoint: string): Observable<T> {
    return this.http.delete<T>(this.buildUrl(service, endpoint));
  }
}
