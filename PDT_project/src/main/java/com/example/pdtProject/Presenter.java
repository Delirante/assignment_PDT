package com.example.pdtProject;

import java.util.ArrayList;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class Presenter {

	DatabaseCalls db;
	
	ArrayList<String> polygons = new ArrayList<String>();

	//Set up polygons from dropdown menu
	public Presenter(DatabaseCalls db) {
		this.db = db;
		//Brtislava
		polygons.add("\r\n" + 
				"'POLYGON((\r\n" + 
				"17.07139 48.26126,\r\n" + 
				"16.955 48.23954,\r\n" + 
				"17.00203 48.14364,\r\n" + 
				"17.06727 48.13562,\r\n" + 
				"17.08546 48.08496,\r\n" + 
				"17.14983 48.08278,\r\n" + 
				"17.26038 48.11683,\r\n" + 
				"17.28219 48.24525,\r\n" + 
				"17.14348 48.28502,\r\n" + 
				"17.07139 48.26126\r\n" + 
				"))'\r\n" + 
				",4326");
		//Centrum
		polygons.add("\r\n" + 
				"'POLYGON((\r\n" +
				"17.07636 48.17261,\r\n" + 
				"17.07533 48.13963,\r\n" + 
				"17.14194 48.13665,\r\n" + 
				"17.14554 48.17536,\r\n" + 
				"17.07636 48.17261\r\n" + 
				"))'\r\n" + 
				",4326");
		//Dubravka
		polygons.add("\r\n" + 
				"'POLYGON((\r\n" +
				"17.02744 48.17101,\r\n" + 
				"17.06349 48.17547,\r\n" + 
				"17.04769 48.18612,\r\n" + 
				"17.0398 48.2042,\r\n" + 
				"17.00701 48.20317,\r\n" + 
				"17.02744 48.17101"+
				"))'\r\n" + 
				",4326");
		//Karlova ves
		polygons.add("\r\n" + 
				"'POLYGON((\r\n" +
				"17.0204 48.14479,\r\n" + 
				"17.07516 48.14238,\r\n" + 
				"17.07396 48.17925,\r\n" + 
				"17.0216 48.17593,\r\n" + 
				"17.0204 48.14479\r\n" + 
				"))'\r\n" + 
				",4326");
		//Petrzalka
		polygons.add("\r\n" + 
				"'POLYGON((\r\n" +
				"17.07258 48.13952,\r\n" + 
				"17.13936 48.1339,\r\n" + 
				"17.13953 48.08301,\r\n" + 
				"17.07757 48.08657,\r\n" + 
				"17.07258 48.13952\r\n" + 
				"))'\r\n" + 
				",4326");
		//Raca
		polygons.add("\r\n" + 
				"'POLYGON((\r\n" +
				"17.10846 48.18978,\r\n" + 
				"17.1306 48.18085,\r\n" + 
				"17.19223 48.19287,\r\n" + 
				"17.17026 48.22627,\r\n" + 
				"17.13867 48.25874,\r\n" + 
				"17.06142 48.23542,\r\n" + 
				"17.10846 48.18978"+
				"))'\r\n" + 
				",4326");
		//Ruzinov
		polygons.add("\r\n" + 
				"'POLYGON((\r\n" +
				"17.22897 48.19585,\r\n" + 
				"17.18709 48.13425,\r\n" + 
				"17.18469 48.0978,\r\n" + 
				"17.14074 48.10422,\r\n" + 
				"17.14091 48.13413,\r\n" + 
				"17.12305 48.14238,\r\n" + 
				"17.12511 48.15841,\r\n" + 
				"17.16958 48.18509,\r\n" + 
				"17.22897 48.19585"+				
				"))'\r\n" + 
				",4326");


		
//Centrum
//17.07636 48.17261
//17.07533 48.13963
//17.14194 48.13665
//17.14554 48.17536
//17.07636 48.17261
		
//Ruzinov
//17.22897 48.19585
//17.18709 48.13425
//17.18469 48.0978
//17.14074 48.10422
//17.14091 48.13413
//17.12305 48.14238
//17.12511 48.15841
//17.16958 48.18509
//17.22897 48.19585

//Petrzalka
//17.07258 48.13952
//17.13936 48.1339
//17.13953 48.08301
//17.07757 48.08657
//17.07258 48.13952
		
//Dubravka
//17.02744 48.17101
//17.06349 48.17547
//17.04769 48.18612
//17.0398 48.2042
//17.00701 48.20317
//17.02744 48.17101
		
//Karlova ves
//17.0204 48.14479
//17.07516 48.14238
//17.07396 48.17925
//17.0216 48.17593
//17.0204 48.14479
		
//Raca		
//17.10846 48.18978
//17.1306 48.18085
//17.19223 48.19287
//17.17026 48.22627
//17.13867 48.25874
//17.06142 48.23542
//17.10846 48.18978 

//Bratislava		
//17.07139 48.26126
//16.955 48.23954
//17.00203 48.14364
//17.06727 48.13562
//17.08546 48.08496
//17.14983 48.08278
//17.26038 48.11683
//17.28219 48.24525
//17.14348 48.28502
//17.07139 48.26126
		
	}
	
	/*
	  http://localhost:8080/searchInArea?doctor=1&dentist=1&hospital=1&clinic=1&
	  pharmacy=1&dropdown=1
	 */
	@RequestMapping("/searchInArea")
	@ResponseBody
	public String searchInArea(@RequestParam(value = "doctor", required = true) boolean doctor,
			@RequestParam(value = "dentist", required = true) boolean dentist,
			@RequestParam(value = "hospital", required = true) boolean hospital,
			@RequestParam(value = "clinic", required = true) boolean clinic,
			@RequestParam(value = "pharmacy", required = true) boolean pharmacy,
			@RequestParam(value = "dropdown", required = true) int dropdown) {
		return db.searchInArea(doctor,dentist,hospital, clinic, pharmacy, polygons.get(dropdown));
	}

	/*
	  http://localhost:8080/searchFromPoint?doctor=1&dentist=1&hospital=1&clinic=1&
	  pharmacy=1&point=text1
	 */
	@RequestMapping("/searchFromPoint")
	@ResponseBody
	public String searchFromPoint(@RequestParam(value = "doctor", required = true) boolean doctor,
			@RequestParam(value = "dentist", required = true) boolean dentist,
			@RequestParam(value = "hospital", required = true) boolean hospital,
			@RequestParam(value = "clinic", required = true) boolean clinic,
			@RequestParam(value = "pharmacy", required = true) boolean pharmacy,
			@RequestParam(value = "point", required = true) String point) {
		return db.searchFromPoint(doctor,dentist,hospital, clinic, pharmacy, point);
	}

	/*
	  http://localhost:8080/searchTriples?doctor=1&dentist=1&hospital=1&clinic=1&
	  pharmacy=1&dropdown=1
	 */
	@RequestMapping("/searchTriples")
	@ResponseBody
	public String searchTriples(@RequestParam(value = "doctor", required = true) boolean doctor,
			@RequestParam(value = "dentist", required = true) boolean dentist,
			@RequestParam(value = "hospital", required = true) boolean hospital,
			@RequestParam(value = "clinic", required = true) boolean clinic,
			@RequestParam(value = "pharmacy", required = true) boolean pharmacy,
			@RequestParam(value = "dropdown", required = true) int dropdown) {
		return db.searchTriples(doctor,dentist,hospital, clinic, pharmacy, polygons.get(dropdown));
	}
	
	@RequestMapping("/coordinatesOfPolygon")
	@ResponseBody
	public String coordinatesOfPolygon(@RequestParam(value = "points", required = true) String points ) {
		return points;
	}
}