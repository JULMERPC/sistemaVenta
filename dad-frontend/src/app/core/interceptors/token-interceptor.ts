// token.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  const token = localStorage.getItem('access_token');
  // const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxtZWFyIiwiaWQiOjEsImlhdCI6MTc1MDcyMzcwMSwiZXhwIjoxNzUwNzI3MzAxfQ.ZX2_ZUYNJwVsGW6EMJj7mBPq4Nt8OB6p59T5ddM0LnI"

  if (token) {
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${token}`
      }
    });
    return next(authReq);
  }

  return next(req);
};
