import {Pipe, PipeTransform} from '@angular/core';

@Pipe({
  name: 'highlightText',
  standalone: true
})
export class HighlightTextPipe implements PipeTransform {

  transform(text: string, searchText: string | null): string {
    if (!searchText) {
      return text;
    }

    // Escape special characters in the search text
    const escapedSearchText = searchText.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');

    // Create a regular expression for the search text, case insensitive
    const re = new RegExp(escapedSearchText, 'gi');

    // Replace the search text with the same text wrapped in <strong> tags
    return text.replace(re, (match) => `<strong>${match}</strong>`);
  }
}
