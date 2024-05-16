import {Component, Input, OnInit} from '@angular/core';
import {NgForOf, NgIf} from "@angular/common";

@Component({
  selector: 'app-star-rating',
  standalone: true,
  imports: [
    NgForOf,
    NgIf
  ],
  templateUrl: './star-rating.component.html',
  styleUrl: './star-rating.component.scss'
})
export class StarRatingComponent implements OnInit {
  @Input({required: true}) rating!: number;
  @Input({required: true}) max!: number;

  fullStars: number = 0;
  blankStars: number = 0;
  halfStar: boolean = false;

  ngOnInit() {
    this.fullStars = Math.round(this.rating)
    this.halfStar = this.fullStars != this.roundHalf(this.rating)
    this.blankStars = this.max - this.fullStars
    if(this.halfStar)
      this.blankStars--
  }

  roundHalf(num: number) {
    return Math.round(num*2)/2;
  }
}
