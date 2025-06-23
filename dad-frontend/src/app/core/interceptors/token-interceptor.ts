// token.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  //const token = localStorage.getItem('access_token');
  const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxtZWFyIiwiaWQiOjEsImlhdCI6MTc1MDcxOTAwNCwiZXhwIjoxNzUwNzIyNjA0fQ.VJTz5hiy7MmvBl9Ib_EdQqIrBNjtbRRQ8qJQIk7ltzg"

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
