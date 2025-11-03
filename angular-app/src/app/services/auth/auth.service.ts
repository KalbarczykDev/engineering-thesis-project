import { Injectable } from '@angular/core';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly key = 'user';

  login(email: string, password: string): boolean {
    if (!email || !password) return false;
    const user = { email, token: 'mock-token' };
    localStorage.setItem(this.key, JSON.stringify(user));
    return true;
  }

  register(email: string, password: string): boolean {
    if (!email || !password) return false;
    const user = { email, token: 'mock-token' };
    localStorage.setItem(this.key, JSON.stringify(user));
    return true;
  }

  logout(): void {
    localStorage.removeItem(this.key);
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem(this.key);
  }

  getUser(): any {
    return JSON.parse(localStorage.getItem(this.key) || 'null');
  }
}
