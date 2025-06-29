// src/app/app.module.ts
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { ReactiveFormsModule, FormsModule } from '@angular/forms';

import { AppRoutingModule } from './app-routing.module';
// @ts-ignore
import { AppComponent } from "./app.component";

// Interceptors
import { provideHttpClient, withInterceptors } from '@angular/common/http';
import { urlInterceptor } from './core/interceptors/url-interceptor';
import { tokenInterceptor } from './core/interceptors/token-interceptor';
import { catchInterceptor } from './core/interceptors/catch-interceptor';

@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    ReactiveFormsModule,
    FormsModule
  ],
  providers: [
    provideHttpClient(
      withInterceptors([
        urlInterceptor,
        tokenInterceptor,
        catchInterceptor
      ])
    )
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
