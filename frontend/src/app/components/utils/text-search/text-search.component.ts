import {Component, EventEmitter, OnInit, Output} from '@angular/core';
import {FormsModule} from "@angular/forms";

@Component({
  selector: 'app-text-search',
  standalone: true,
  imports: [
    FormsModule
  ],
  templateUrl: './text-search.component.html',
  styleUrl: './text-search.component.scss'
})
export class TextSearchComponent implements OnInit {
  @Output() onChange: EventEmitter<string | null> = new EventEmitter<string | null>();
  searchText: string | null = null;

  ngOnInit() {

  }

}
