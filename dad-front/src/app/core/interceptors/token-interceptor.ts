// token.interceptor.ts
import { HttpInterceptorFn } from '@angular/common/http';

export const tokenInterceptor: HttpInterceptorFn = (req, next) => {
  //const token = localStorage.getItem('access_token');
  const token = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqdWxtZWFyIiwiaWQiOjEsImlhdCI6MTc1MDcyOTA1NCwiZXhwIjoxNzUwNzMyNjU0fQ.jpQDs-heAweJ96bJsbuQ_XwnzdMNJSm0-fJ07SIXgms"

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
