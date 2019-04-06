import { Component } from '@angular/core';
import { DataService } from './services/data.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'frontend';
  meds: Array<any>;
  patient: any;

  constructor(public dataService: DataService) {
    this.dataService.meds.then(meds => this.meds = meds as any);
    this.dataService.patient.then(patient => this.patient = patient);
  }

  formatDosage(dosage) {
    return dosage.map(item => item.text).join(',');
  }

  getMobile(patient) {
    return patient.telecom.filter(phone => phone.use == 'mobile')[0].value;
  }

  getActiveMeds(meds) {
    const allMeds = [];
    for (const resource of meds) {
      for (let med of resource.entry) {
        allMeds.push(med);
      }
    }
    const activeMeds = allMeds.filter(med => med.resource.status == 'active');
    return activeMeds;
  }

  getInactiveMeds(meds) {
    const allMeds = [];
    for (const resource of meds) {
      for (let med of resource.entry) {
        allMeds.push(med);
      }
    }
    const inactiveMeds = allMeds.filter(med => med.resource.status != 'active');
    return inactiveMeds;
  }
}
