import { Component, OnDestroy } from '@angular/core';
import { ApiService } from '../api.service';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MatIconModule } from '@angular/material/icon';
import { Subscription } from 'rxjs';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [MatFormFieldModule, MatProgressBarModule, MatTooltipModule, MatInputModule, MatIconModule, CommonModule],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.scss'
})
export class UploadComponent implements OnDestroy {
  selectedFile: File | null = null;
  uploadProgress: number | null = null;
  uploadError: string | null = null;
  private uploadSubscription: Subscription | undefined;

  constructor(private apiService: ApiService) {}

  ngOnDestroy(): void {
    // Unsubscribe from upload observable to prevent memory leaks
    if (this.uploadSubscription) {
      this.uploadSubscription.unsubscribe();
    }
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
    this.uploadProgress = null;
    this.uploadError = null; // Clear any previous errors
  }

  onSubmit() {
    if (this.selectedFile) {
      const formData = new FormData();
      formData.append('file', this.selectedFile);

      // Subscribe to the upload observable and store the subscription
      this.uploadSubscription = this.apiService.uploadExercises(formData)
        .subscribe(
          exercises => {
            console.log('Exercises uploaded successfully:', exercises);
            // Handle successful upload, e.g., clear form, display success message
            this.selectedFile = null;
            this.uploadProgress = null;
            // Reset file input to allow selecting the same file again
            this.resetFileInput();
          },
          error => {
            console.error('Error uploading exercises:', error);
            // Handle upload error, e.g., display error message
            this.uploadError = error || 'An error occurred during upload.';
          },
          () => {
            console.log('Upload completed');
            // Reset file input to allow selecting the same file again
            this.resetFileInput();
          }
        );
    } else {
      console.warn('Please select a file to upload.');
    }
  }

  private resetFileInput() {
    // Reset file input value to allow selecting the same file again
    const fileInput = document.querySelector('.file-input') as HTMLInputElement;
    if (fileInput) {
      fileInput.value = '';
    }
  }
}
