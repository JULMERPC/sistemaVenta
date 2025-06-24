import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';
import { provideRouter } from '@angular/router';

import { routes } from './app.routes';
import { provideAnimationsAsync } from '@angular/platform-browser/animations/async';
import {provideHttpClient, withFetch, withInterceptors} from "@angular/common/http";
import {catchInterceptor} from "./interceptors/catch.interceptor";
import {urlInterceptor} from "./interceptors/url.interceptor";
import {tokenInterceptor} from "./interceptors/token.interceptor";

export const appConfig: ApplicationConfig = {
  providers: [provideZoneChangeDetection({ eventCoalescing: true }),
    provideRouter(routes),
    provideHttpClient(withFetch()),
    provideHttpClient(
      withInterceptors([
        tokenInterceptor,
        urlInterceptor,
        catchInterceptor
      ])
    ),
    provideAnimationsAsync()]

};
