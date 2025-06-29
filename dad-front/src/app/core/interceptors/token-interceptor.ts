// token.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  //const token = localStorage.getItem('access_token');
  const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxtZWFyIiwiaWQiOjEsImlhdCI6MTc1MTIyODgwOCwiZXhwIjoxNzUxMjMyNDA4fQ.9kEfpyEY86WSSUFAkNkP0bcKZFnSFLWOAy9dj4IN58o"

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
