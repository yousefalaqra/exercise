import { Injectable } from '@angular/core';
import { Observable, catchError, throwError } from 'rxjs';
import { Exercise } from './model/exercise';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ApiService {
  constructor(private http: HttpClient) {}

  uploadExercises(formData: FormData): Observable<Exercise[]> {

    return this.http
      .post<Exercise[]>('http://localhost:8080/api/exercises/upload', formData, {
        reportProgress: true, // Enable progress reporting (optional)
        observe: 'body',
      })
      .pipe(catchError(this.handleError));
  }

  // Get All Exercises
  getAllExercises(): Observable<Exercise[]> {
    return this.http
      .get<Exercise[]>('http://localhost:8080/api/exercises/')
      .pipe(catchError(this.handleError));
  }

  // Get Exercise by Code
  getExerciseByCode(code: string): Observable<Exercise> {
    return this.http
      .get<Exercise>(`http://localhost:8080/api/exercises/${code}`)
      .pipe(catchError(this.handleError));
  }

  // Delete All Exercises
  deleteAllExercises(): Observable<string> {
    return this.http
      .delete<string>('http://localhost:8080/api/exercises/')
      .pipe();
  }

  // Error Handling
  private handleError(error: HttpErrorResponse): Observable<never> {
    if (error.error instanceof ErrorEvent) {
      // Client-side or network error occurred. Handle it accordingly.
      console.error('An error occurred:', error.error.message);
    } else {
      // The backend returned an unsuccessful response code.
      // The response body may contain clues as to what went wrong.
      console.error(
        `Backend returned code ${error.status}, ` + `body was: ${error.error}`
      );
    }
    // Return an observable with a user-facing error message.
    return throwError('Something went wrong, please try again later.');
  }
}
