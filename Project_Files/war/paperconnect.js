function initPaperTable(elementId, flag) {

	// Clear the table
	document.getElementById(elementId).innerHTML = "";

	// Create table header
	var tableHeader = document.createElement('LI');
	var title = document.createElement("DIV");
	var author = document.createElement("DIV");
	var publishDate = document.createElement("DIV");
	var abstract = document.createElement("DIV");

	tableHeader.className = "table-header";
	title.className = "col col-1";
	author.className = "col col-2";
	publishDate.className = "col col-3";
	abstract.className = "col col-4";

	title.innerHTML = "TITLE";
	author.innerHTML = "AUTHOR";
	publishDate.innerHTML = "PUBLISH DATE";
	abstract.innerHTML = "ABSTRACT";
	
	tableHeader.appendChild(title);
	tableHeader.appendChild(author);
	tableHeader.appendChild(publishDate);
	if(flag){
		tableHeader.appendChild(abstract);
	}
	document.getElementById(elementId).appendChild(tableHeader);
}

function addPaperToTable(obj, elementId, flag) {
	var paper = JSON.parse(obj);
	
	// Create paperList table rows
	var tableRow = document.createElement('LI');
	var paperTitle = document.createElement("DIV");
	var paperAuthor = document.createElement("DIV");
	var paperPublishDate = document.createElement("DIV");
	var paperAbstract = document.createElement("DIV");
	var data_label_title = document.createAttribute("data-label");
	var data_label_author = document.createAttribute("data-label");
	var data_label_publishDate = document.createAttribute("data-label");
	var data_label_abstract = document.createAttribute("data-label");
	
	tableRow.className = "table-row";
	paperTitle.className = "col col-1";
	paperAuthor.className = "col col-2";
	paperPublishDate.className = "col col-3";
	paperAbstract.className = "col col-4";

	data_label_title.value = "Title";
	paperTitle.setAttributeNode(data_label_title);

	data_label_author.value = "Author";
	paperAuthor.setAttributeNode(data_label_author);

	data_label_publishDate.value = "Publish Date";
	paperPublishDate.setAttributeNode(data_label_publishDate);

	data_label_abstract.value = "Abstract";
	paperAbstract.setAttributeNode(data_label_abstract);
	
	paperTitle.innerHTML = paper.title;
	tableRow.appendChild(paperTitle);
	
	paperAuthor.innerHTML = paper.author;
	tableRow.appendChild(paperAuthor);

	paperPublishDate.innerHTML = paper.publishDate;
	tableRow.appendChild(paperPublishDate);
	
	if(flag){
		paperAbstract.innerHTML = paper.abstract;
		tableRow.appendChild(paperAbstract);	
	}
	document.getElementById(elementId).appendChild(tableRow);
}

function addRowHandlers() {
	var table = document.getElementById("paperTable");
	var rows = table.getElementsByTagName("LI");
	for (i = 1; i < rows.length; i++) {
		var currentRow = rows[i];
		var divTags = rows[i].getElementsByTagName("DIV");
	    var currentRowData = divTags[0].innerHTML;
	    var createClickHandler = function(title) {
	        return function() {
	        	console.log(title);
	        	window.onTableClick(title);
	        };
	    };
	    currentRow.onclick = createClickHandler(currentRowData);
	}
}

function drawGraph(obj, elementId) {
	document.getElementById(elementId).innerHTML = ""; // Initialize sigma:
	var s = new sigma({
		renderer : {
			container : document.getElementById(elementId),
			type : 'canvas'
		},
		settings : {}
	});

	s.graph.clear();

	var graph = {
		nodes : [],
		edges : []
	};
	var jsonGraph = JSON.parse(obj);
	console.log(obj.toString());

	for (i = 0; i < jsonGraph.nodes.length; i++) {
		graph.nodes.push({
			id : jsonGraph.nodes[i].id,
			label : jsonGraph.nodes[i].label,
			x : jsonGraph.nodes[i].x,
			y : jsonGraph.nodes[i].y,
			size : jsonGraph.nodes[i].size,
			color: '#008CC2'
		});
	}
	for (i = 0; i < jsonGraph.edges.length; i++) {
		console.log(jsonGraph.edges[i].source);
		console.log(jsonGraph.edges[i].target);
		graph.edges.push({
			id : jsonGraph.edges[i].id,
			source : jsonGraph.edges[i].source,
			target : jsonGraph.edges[i].target,
			 color: '#DC143C',
		});
	}

	// Load the graph in sigma
	s.graph.read(graph);
	// Ask sigma to draw it
	s.refresh();
}