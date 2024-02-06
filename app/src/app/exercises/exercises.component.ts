import { Component, OnDestroy, OnInit } from '@angular/core';
import { Exercise } from '../model/exercise';
import { ApiService } from '../api.service';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-exercises',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './exercises.component.html',
  styleUrl: './exercises.component.scss'
})
export class ExercisesComponent implements OnInit, OnDestroy {
  exercises: Exercise[] = [];
  exerciseByCode: Exercise | undefined;
  errorMessage: string = '';
  loading: boolean = false;
  private subscriptions: Subscription[] = [];

  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
    this.getAllExercises();
  }

  getAllExercises(): void {
    this.loading = true;
    const subscription = this.apiService.getAllExercises().subscribe(
      exercises => {
        this.exercises = exercises;
        this.loading = false;
      },
      error => {
        this.errorMessage = error;
        this.loading = false;
      }
    );
    this.subscriptions.push(subscription);
  }

  getExerciseByCode(code: string): void {
    this.loading = true;
    const subscription = this.apiService.getExerciseByCode(code).subscribe(
      exercise => {
        this.exerciseByCode = exercise;
        this.loading = false;
      },
      error => {
        this.errorMessage = error;
        this.loading = false;
      }
    );
    this.subscriptions.push(subscription);
  }

  deleteAll(): void {
    this.loading = true;
    const subscription = this.apiService.deleteAllExercises().subscribe(
      (response: any) => {
        // Upon successful deletion, remove the exercise from the array
        this.exercises = []
        this.loading = false;
        this.errorMessage = '';
      },
      (error: any) => {
        this.exercises = []
        this.loading = false;
        this.errorMessage = '';
      }
    );
    this.subscriptions.push(subscription);
  }

  ngOnDestroy(): void {
    // Unsubscribe from all subscriptions to prevent memory leaks
    this.subscriptions.forEach(subscription => subscription.unsubscribe());
  }
}