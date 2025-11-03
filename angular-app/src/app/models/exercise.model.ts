export interface Exercise {
  id: number;
  name: string;
  category: string;
  muscleGroup: string;
  equipment: string;
  description?: string;
  imageUrl?: string;
  createdAt: Date;
  lastUpdateAt: Date;
}
