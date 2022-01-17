import { Employee } from '../employee';

export class Message {
    employees: Employee[];
    totalPages: number;
    pageNumber: number;
    pageSize: number;
}