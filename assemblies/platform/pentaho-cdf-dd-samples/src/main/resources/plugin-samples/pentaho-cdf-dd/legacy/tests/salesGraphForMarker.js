/*! ******************************************************************************
 *
 * Pentaho
 *
 * Copyright (C) 2024 by Hitachi Vantara, LLC : http://www.pentaho.com
 *
 * Use of this software is governed by the Business Source License included
 * in the LICENSE.TXT file.
 *
 * Change Date: 2029-07-20
 ******************************************************************************/
lib('protovis-bundle.js');

var render_salesGraphForMarker = {
	type: "cccPieChart",
	name: "render_salesGraphForMarker",
	priority: 5,
	parameters: [["customer","CustomerNameParameter"]],
	executeAtStart: true,
	listeners: [],
	chartDefinition:  {
		dataAccessId: "salesGraphQuery",
		path: "/public/plugin-samples/pentaho-cdf-dd/legacy/tests/NewMapComponent/FullMapTest.cda",
		width: 75,
		height: 75,
		extensionPoints: [],
		colors: [],
		activeSliceRadius: "5%",
		animate: false,
		clearSelectionMode: "emptySpaceClick",
		clickable: false,
		compatVersion: 2,
		contentMargins: "0",
		contentPaddings: "0",
		crosstabMode: false,
		ctrlSelectMode: true,
		dataIgnoreMetadataLabels: false,
		dataMeasuresInColumns: false,
		dataSeparator: "~",
		explodedSliceIndex: 0,
		explodedSliceRadius: "0",
		groupedLabelSep: " ~ ",
		hoverable: false,
		ignoreNulls: true,
		isMultiValued: false,
		legend: false,
		legendAlign: "center",
		legendClickMode: "toggleVisible",
		legendFont: "10px sans-serif",
		legendItemPadding: 2.5,
		legendMargins: "0",
		legendMarkerSize: 15,
		legendPaddings: "5",
		legendPosition: "bottom",
		legendTextMargin: 6,
		legendVisible: true,
		linkHandleWidth: 0.5,
		linkInsetRadius: "5%",
		linkLabelSize: "15%",
		linkLabelSpacingMin: 0.5,
		linkMargin: "2.5%",
		linkOutsetRadius: "2.5%",
		margins: "3",
		measuresIndexes: [],
		multiChartColumnsMax: 3,
		multiChartIndexes: [],
		multiChartOverflow: "grow",
		multiChartSingleColFillsHeight: true,
		multiChartSingleRowFillsHeight: true,
		paddings: "0",
		selectable: false,
		seriesInRows: false,
		smallContentMargins: "0",
		smallContentPaddings: "0",
		smallMargins: "2%",
		smallPaddings: "0",
		smallTitleFont: "14px sans-serif",
		smallTitleMargins: "0",
		smallTitlePaddings: "0",
		smallTitlePosition: "top",
		timeSeries: false,
		timeSeriesFormat: "%Y-%m-%d",
		titleFont: "14px sans-serif",
		titleMargins: "0",
		titlePaddings: "0",
		titlePosition: "top",
		titleSize: "25",
		tooltipEnabled: true,
		tooltipFade: true,
		tooltipFollowMouse: false,
		tooltipHtml: true,
		tooltipOpacity: 0.9,
		valuesAnchor: "right",
		valuesFont: "10px sans-serif",
		valuesLabelStyle: "linked",
		valuesVisible: false,
		orientation: "vertical"
	}
};

cgg.initParameter
("customer", "none")
;

cgg.render(render_salesGraphForMarker);
