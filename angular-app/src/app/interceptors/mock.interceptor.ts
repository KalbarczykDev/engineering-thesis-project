import { Injectable } from '@angular/core';
import {
  HttpInterceptorFn,
  HttpRequest,
  HttpHandlerFn
} from '@angular/common/http';
import { environment } from '../../environments/environment';

export const mockInterceptor: HttpInterceptorFn = (req: HttpRequest<any>, next: HttpHandlerFn) => {
  if (environment.useMockData) {
    const mapped = mapToMock(req.url);
    if (mapped) {
      const clone = req.clone({ url: mapped });
      return next(clone);
    }
  }
  return next(req);
};

function mapToMock(url: string): string | null {
  if (url.endsWith('/exercises')) return '/assets/mock-data/exercises.json';
  //TODO: Add more mappings as needed
  return null;
}
