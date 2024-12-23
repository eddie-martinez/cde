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

define([
  "cdf/AddIn",
  "cdf/Dashboard.Clean",
  "cdf/lib/jquery",
  "amd!cdf/lib/underscore"
], function(AddIn, Dashboard, $, _) {
  "use strict";
  var kml = {
    name: "kml",
    label: "KML shape resolver",
    defaults: {
      url: "", //url for the resource containing the kml data
      idSelector: "name",
      parseShapeKey: null
    },
    implementation: function(tgt, st, opt) {
      var deferred = $.Deferred();
      var url = opt.url || st._shapeSource,
        parseShapeKey = opt.parseShapeKey || st._parseShapeKey;

      if (url) {
        $.ajax(url, {
          async: true,
          type: "GET",
          processData: false,
          success: function(data) {
            deferred.resolve(getShapeFromKML(data, opt.idSelector, parseShapeKey));
          },
          error: function() {
            deferred.resolve({});
          }
        });
      } else {
        deferred.resolve(null);
      }
      return deferred.promise();
    }
  };

  function getShapeFromKML(rawData, idSelector, parseShapeKey) {
    /*
     Parse a KML file, return a JSON dictionary where each key is associated with an array of shapes of the form
     mymap = {'Cascais:'[ [[lat0, long0],[lat1, long1]] ]}; // 1 array with a list of points
     */
    var mymap = {};

    $(rawData).find("Placemark").each(function(idx, y) {
      var key;
      if (_.isFunction(parseShapeKey)) {
        try {
          key = parseShapeKey(y);
        } catch (e) {
          key = $(y).find(idSelector).text();
        }
      } else {
        key = $(y).find(idSelector).text();
      }

      // Create an array for the strings that define the (closed) curves in a Placemark
      var polygonArray = _.map($(y).find("Polygon"), function(yy) {
        var polygon = [];
        _.each(["outerBoundaryIs", "innerBoundaryIs"], function(b) {
          var polygonObj = $(yy).find(b + " LinearRing coordinates");
          //if(polygonObj.length >0) {
          _.each(polygonObj, function(v) {
            var s = $(v).text().trim();
            if (s.length > 0) {
              var p = _.map(s.split(" "), function(el) {
                return _.map(el.split(",").slice(0, 2), parseFloat);//.reverse();
              });
              //p =  this.reducePoints(p.slice(0, pp.length -1), precision_m); // this would reduce the number of points in the shape
              polygon.push(p);
            }
          });
          //}
        });
        return polygon;
      });
      if (_.isEmpty(polygonArray)) {
        return;
      }
      if (!mymap[key]) {
        mymap[key] = multiPolygonToGeoJSON(polygonArray);
      }
    });

    return mymap;
  }

  function multiPolygonToGeoJSON(polygonArray) {
    return {
      type: "Feature",
      geometry: {
        type: "MultiPolygon",
        coordinates: polygonArray
      },
      properties: {}
    };
  }

  Dashboard.registerGlobalAddIn("NewMapComponent", "ShapeResolver", new AddIn(kml));

  return kml;

});
