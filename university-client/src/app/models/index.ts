export interface UserProfile {
  id: string;
  username: string;
  email: string;
  firstName: string;
  lastName: string;
  roles: string[];
}

export interface Student {
  id: string;
  studentNumber: string;
  userId: string;
  currentSemester: number;
}

export interface Teacher {
  id: string;
  userId: string;
  department: string;
}

export interface Course {
  id: string;
  name: string;
  credits: number;
  semester: number;
}

export interface Note {
  id: string;
  value: number;
  type: 'EXAM' | 'ASSIGNMENT';
  courseId: string;
  studentId: string;
  date: string;
}
