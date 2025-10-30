import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Workout } from '../models/workout';

export interface User {
  username: string;
  email: string;
}

@Injectable({
  providedIn: 'root'
})
export class ProfileService {

  constructor(private http: HttpClient) {
  }

  getUser(): Observable<User> {
    return this.http.get<User>('/profile/user');
  }

  getWorkouts(): Observable<Workout[]> {
    return this.http.get<Workout[]>('/profile/workouts');
  }
}
