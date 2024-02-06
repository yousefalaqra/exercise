import { Component } from '@angular/core';
import { ApiService } from '../api.service';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule } from '@angular/material/input';
import {MatProgressBarModule} from '@angular/material/progress-bar';
import {MatTooltipModule} from '@angular/material/tooltip';
import {MatIconModule} from '@angular/material/icon';




@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [MatFormFieldModule, MatProgressBarModule, MatTooltipModule, MatInputModule, MatIconModule],
  templateUrl: './upload.component.html',
  styleUrl: './upload.component.scss'
})
export class UploadComponent {
  selectedFile: File | null = null;
  uploadProgress: number | null = null;
  uploadError: string | null = null;


  constructor(private apiService: ApiService) {}

  ngOnInit(): void {
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

      this.apiService.uploadExercises(formData)
        .subscribe(
          exercises => {
            console.log('Exercises uploaded successfully:', exercises);
            // Handle successful upload, e.g., clear form, display success message
            this.selectedFile = null;
            this.uploadProgress = null;
          },
          error => {
            console.error('Error uploading exercises:', error);
            // Handle upload error, e.g., display error message
            this.uploadError = error.message || 'An error occurred during upload.';
          },
          () => {
            console.log('Upload completed');
            // Optional: Perform actions after upload completes, regardless of success/error
          }
        );
    } else {
      console.warn('Please select a file to upload.');
    }
  }

}
