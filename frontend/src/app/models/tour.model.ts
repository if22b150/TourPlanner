import {AModel} from "./a-model";
import {TourLogModel} from "./tour-log.model";

export interface TourModel extends AModel {
  name: string,
  description: string,
  from: string,
  to: string,
  transportType: string,
  distance: number,
  estimatedTime: number,
  imagePath: string,
  tourLogs?: TourLogModel[]
}
