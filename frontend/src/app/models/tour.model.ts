import {AModel} from "./a-model";

export interface TourModel extends AModel {
  id: number,
  name: string,
  description: string,
  from: string,
  to: string,
  transportType: string,
  distance: number,
  estimatedTime: number,
  imagePath: string
}
