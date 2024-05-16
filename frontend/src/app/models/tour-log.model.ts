import {AModel} from "./a-model";
import {TourModel} from "./tour.model";

export interface TourLogModel extends AModel {
  date: string,
  comment: string,
  difficulty: number,
  totalDistance: number,
  totalTime: number,
  rating: number,
  tour?: TourModel
}
