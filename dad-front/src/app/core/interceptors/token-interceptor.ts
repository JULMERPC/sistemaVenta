// token.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  //const token = localStorage.getItem('access_token');
  const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxtZWFyIiwiaWQiOjEsImlhdCI6MTc1MDcyODU2OSwiZXhwIjoxNzUwNzMyMTY5fQ.asV957o5ELg4-F5ikFA0SPq6tGEZLMK_CwdEz3zmaPI"

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
