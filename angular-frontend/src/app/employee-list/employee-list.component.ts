import { Component, OnInit, VERSION, Pipe, PipeTransform, Injectable} from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

import { Employee } from '../employee'
import { EmployeeService } from '../employee.service'
import { Router } from '@angular/router';
import { map, withLatestFrom, startWith, tap } from 'rxjs/operators';
import { of, Observable } from 'rxjs';
import { Message } from './message';
@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent implements OnInit {

  employees: Employee[];
  filteredFoods$: Observable<Employee[]> | undefined;
  foods$: Observable<Employee[]>;
  filteredEmployee: Employee[];
  currentSelectedPage:number = 0;
  totalPages: number = 0;
  pageIndexes: Array<number> = [];

 

  searchString: string;
  filter:string;
  formGroup: FormGroup;
  pageNumber: number;
  pageSize: number;

  constructor(private employeeService: EmployeeService,
    private router: Router,private formBuilder: FormBuilder ) { 

      this.formGroup = formBuilder.group({ filter: [''] });

      this.filteredFoods$ = this.formGroup.get('filter')?.valueChanges.pipe(
        startWith(''),
        withLatestFrom(this.foods$),
        map(([val, foods]) =>
          !val ? foods : foods.filter((x) => x.firstName.toLowerCase().includes(val))
        )
      );
    }

  ngOnInit(): void {
    this.getEmployees();
    this.getPage(0);

  }

  private getEmployees(){
    this.employeeService.getEmployeesList().subscribe(data => {
      this.employees = data;
    });
  }

  employeeDetails(id: number){
    this.router.navigate(['employee-details', id]);
  }

  updateEmployee(id: number){
    this.router.navigate(['update-employee', id]);
  }

  deleteEmployee(id: number){
    this.employeeService.deleteEmployee(id).subscribe( data => {
      console.log(data);
      this.getEmployees();
    })
  }

  getPage(page: number){

    this.employeeService.getPagableEmployee(page, this.pageSize)
            .subscribe(
                (message: Message) => {
                  console.log(message);
                  this.employees = message.employees;
                  this.totalPages = message.totalPages;
                  this.pageIndexes = Array(this.totalPages).fill(0).map((x,i)=>i);
                  this.currentSelectedPage = message.pageNumber;
                },
                (error) => {
                  console.log(error);
                }
            );
  }
  nextClick(){
    if(this.currentSelectedPage < this.totalPages-1){
      this.getPage(++this.currentSelectedPage);
    }  
  }

  previousClick(){
    if(this.currentSelectedPage > 0){
      this.getPage(--this.currentSelectedPage);
    }  
  }
}

