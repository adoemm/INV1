<%-- 
    Document   : testTableSort
    Created on : Apr 5, 2013, 12:38:43 AM
    Author     : Administrator
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <script type="text/javascript" language="javascript" src="http://code.jquery.com/jquery-latest.js"></script>

        

        <script type="text/javascript" language="javascript">

            // <reference path="jquery-1.4.1.js" />
// <reference path="jquery-ui-1.8.12.min.js" />
// <reference path="jquery.scrollTo.js" />
// <reference path="jquery.caret.js" />
            (function($) {
            var jqGridSearchElementCount = 0;
                    var DOWN = 40, UP = 38, LEFT = 37, RIGHT = 39, ENTER = 13;
                    var defaultItems = {
            input: function(container, settings) {
            var img = $('<span></span>').attr("id", "clearInput").addClass("ui-button-icon-secondary ui-icon ui-icon-circle-close").css({
            position: "absolute",
                    "margin-top": - 8
            }).click(function() {
            $("#" + settings.keyItem).val("");
                    $(this).hide();
                    container.gridSearch(settings.primaryAction);
            }).hide();
                    var span = $("<span></span>")
                    .addClass("ui-button ui-widget ui-state-default ui-corner-all ui-button-text-icon-secondary")
                    .css({
            position: "static"
            });
                    var input = $('<input type="text" style=\"height:20px\"/>').val(resources.searchText)
                    .attr({
            id: settings.keyItem
            })
                    .keydown(function(e) {
            if (e.keyCode == ENTER) {
            container.gridSearch('filter');
                    e.preventDefault();
            }
            })
                    .keyup(function(e) {
            if (e.keyCode == UP)
                    container.gridSearch('before');
                    else if (e.keyCode == ENTER) {
            container.gridSearch('filter');
                    e.preventDefault();
            }
            else
                    container.gridSearch(settings.primaryAction);
                    if ($(this).val() != "")
                    $("#clearInput").show();
                    else
                    $("#clearInput").hide();
            })
                    .addClass(settings.keyItemClass)
                    .addClass("ui-widget ui-state-default ui-button-text-icon-primary")
                    .focus(function() {
            $(this).val(function() {
            if ($(this).val() != resources.searchText)
                    return $(this).val();
                    return "";
            }).removeClass("ui-state-default").addClass("ui-state-active")
                    var len = $(this).val().length;
                    $(this).caret({start: 0, end: len})
            })
                    .blur(function() {
            $(this).val(function() {
            if ($.trim($(this).val()) == "")
                    return resources.searchText;
                    return $(this).val();
            })
                    .removeClass("ui-state-active").addClass("ui-state-default")
            })

                    return span.append(input)
                    .append(img);
            },
                    beforeButton: function(container, settings) {
            return $('<button>' + resources.buttons.before + '</button>').attr({
            id: settings.buttons.before.selector,
                    value: resources.buttons.before
            }).button({
            icons: {
            primary: "ui-icon-carat-1-w"
            },
                    text: settings.textVisible.before
            }).click(function(e) {
            container.gridSearch('before');
                    e.preventDefault();
            });
            },
                    nextButton: function(container, settings) {
            return $('<button>' + resources.buttons.next + '</button>').attr({
            id: settings.buttons.next.selector,
                    value: resources.buttons.next
            }).button({
            icons: {
            secondary: "ui-icon-carat-1-e"
            },
                    text: settings.textVisible.next
            }).click(function(e) {
            container.gridSearch('next');
                    e.preventDefault();
            });
            },
                    filterButton: function(container, settings) {
            return $('<button>' + resources.buttons.filter + '</button>').attr({
            id: settings.buttons.filter.selector,
                    value: resources.buttons.filter
            }).button({
            icons: {
            primary: "ui-icon-search"
            },
                    text: settings.textVisible.filter
            }).click(function(e) {
            container.gridSearch('filter');
                    e.preventDefault();
            });
            },
                    unfilterButton: function(container, settings) {
            return $('<button>' + resources.buttons.unfilter + '</button>').attr({
            id: settings.buttons.unfilter.selector,
                    value: resources.buttons.unfilter
            }).button({
            icons: {
            primary: "ui-icon-arrowrefresh-1-w"
            },
                    text: settings.textVisible.unfilter
            }).click(function(e) {
            container.gridSearch('unfilter');
                    e.preventDefault();
            });
            }
            }

            var methods = {
            destroy: function() {
            var settings = this.data("jqGridSearchPlugin");
                    if (settings.searchBarAtBottom)
                    this.remove("." + styles.searchBar.bottom);
                    else
                    this.remove("." + styles.searchBar.header);
            },
                    init: function(options) {
            _container = this;
                    settings = {
            primaryAction: "search",
                    searchableInput: '[searchable]',
                    headerContainer: 'gridSearchHeader_' + jqGridSearchElementCount,
                    keyItem: 'gridSearchInput_' + jqGridSearchElementCount,
                    scrollDuration: 400,
                    buttonContainerHeaderClass: 'gridViewButtonContainerHeader_' + jqGridSearchElementCount,
                    buttonContainerFooterClass: 'gridViewButtonContainerFooter_' + jqGridSearchElementCount,
                    keyItemClass: 'gridViewInput_' + jqGridSearchElementCount,
                    searchBarAtBottom: true,
                    minCount: 0,
                    customScrollHeight: 0,
                    visible: {
            before: true,
                    next: true,
                    filter: true,
                    unfilter: true
            },
                    textVisible: {
            before: true,
                    next: true,
                    filter: true,
                    unfilter: true
            },
                    buttons: {
            before: {selector: "gridSearchBefore_" + jqGridSearchElementCount},
                    next: {selector: "gridSearchNext_" + jqGridSearchElementCount},
                    filter: {selector: "gridSearchFilter_" + jqGridSearchElementCount},
                    unfilter: {selector: "gridSearchUnfilter_" + jqGridSearchElementCount}
            },
                    _foundItemIdx: 0,
                    _found: false,
                    _foundItemsArray: new Array(),
                    _searchCriteria: "",
                    _searchInNthColumn: - 1
            };
                    $.extend(settings, options);
                    this.data("jqGridSearchPlugin", settings);
                    this.gridSearch('destroy');
                    var initItems = function(container, settings) {
            var div = $("<div style='border-radius:0px;margin:0px !important'></div>").addClass("ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-state-hover");
                    var ul = $("<ul style='float:right'></ul>");
                    if ($.browser.version == "7.0")
                    ul.addClass("displayInline")
                    var spanLeft = $("<li style='float:left'></li>").addClass("ui-state-hover");
                    var spanRight = $("<li style='float:left;'></li>").addClass("ui-state-hover ui-tabs-selected");
                    var columnHeaders = getColumnHeaders(container);
                    var dd = $("<select id='jqGridSearchColumnSelector_" + jqGridSearchElementCount + "' style='height:25px'></select>");
                    dd.append("<option value='-1'>" + resources.searchAllColumn + "</option>");
                    for (k = 0; k < columnHeaders.length; k++) {

            if (columnHeaders[k] != "")
                    dd.append("<option value='" + (k + 1) + "'>" + columnHeaders[k] + "</option>");
            }
            dd.change(function() {
            container.gridSearch("search");
            }).addClass("ui-widget ui-state-default");
                    spanLeft.append(defaultItems.input(container, settings).addClass("ui-button ui-button-text ui-state-default"));
                    if (columnHeaders.length > 1)
                    spanLeft.append(dd);
                    if (settings.visible.before)
                    spanRight.append(defaultItems.beforeButton(container, settings));
                    if (settings.visible.next)
                    spanRight.append(defaultItems.nextButton(container, settings));
                    if (settings.primaryAction != "filter" && settings.visible.filter)
                    spanRight.append(defaultItems.filterButton(container, settings));
                    if (settings.visible.unfilter)
                    spanRight.append(defaultItems.unfilterButton(container, settings));
                    ul.append(spanLeft).append(spanRight);
                    div.append(ul);
                    return div.addClass(function() {
            if (settings.searchBarAtBottom)
                    return styles.searchBar.bottom;
                    return styles.searchBar.top;
            });
            };
                    if (settings.searchBarAtBottom)
                    this.after(initItems(this, settings).addClass(settings.buttonContainerFooterClass));
                    else
                    this.before(initItems(this, settings).addClass(settings.buttonContainerHeaderClass));
                    enableDisableButtons(settings);
                    jqGridSearchElementCount++;
            },
                    search: function() {
            var settings = this.data("jqGridSearchPlugin");
                    container = this;
                    var found = false;
                    var keyItem = $("#" + settings.keyItem);
                    settings._foundItemsArray = new Array();
                    if (settings.searchCriteria != keyItem.val()) {
            settings._foundItemIdx = 0;
                    this.find("." + styles.findSubstring).removeClass(styles.findSubstring);
            }
            if (keyItem.val() != resources.searchText)
                    settings.searchCriteria = keyItem.val();
                    if (keyItem.val() != "" && keyItem.val().length > settings.minCount) {
            //Set searchable columns
            settings._searchInNthColumn = $("#jqGridSearchColumnSelector_" + (jqGridSearchElementCount - 1) + " option:selected").val();
                    var counter = 0;
                    var selector;
                    if (settings._searchInNthColumn == - 1)
                    selector = this.contents().find("tbody td");
                    else {
            selector = this.find("table tbody tr td:nth-child(" + settings._searchInNthColumn + ")");
            }
            selector.each(function() {
            var $this = $(this);
                    var idxOfCriteria = trToUpper($this.text()).indexOf(trToUpper(keyItem.val()))
                    if ($this.is(":visible") && idxOfCriteria >= 0) {
            settings._foundItemsArray.push($this);
                    if (settings._foundItemIdx == counter) {
            settings._foundItemIdx = ++counter;
                    //wrapText($this, idxOfCriteria, idxOfCriteria + keyItem.val().length);
                    unhighlightRow(container);
                    highlightRow(container, $this, settings.customScrollHeight);
                    found = true;
                    return false;
            }
            counter++;
            }
            });
                    if (settings._foundItemIdx != counter)
                    alert(resources.endOfSearch);
                    if (settings._foundItemsArray.length == 0)
                    unhighlightRow(container);
                    else
                    $('.' + styles.noRecordFound).hide();
            }
            else
                    unhighlightRow(this);
                    enableDisableButtons(settings);
            },
                    next: function(options) {
            this.gridSearch('search');
            },
                    before: function(options) {
            var settings = this.data("jqGridSearchPlugin");
                    if (settings._foundItemIdx == 0)
                    alert(resources.noRecordFound);
                    else if (--settings._foundItemIdx == 0)
                    alert(resources.endOfSearch);
                    else {
            beforeItem = settings._foundItemsArray[settings._foundItemIdx - 1];
                    unhighlightRow(this);
                    highlightRow(this, beforeItem, settings.customScrollHeight);
            }
            enableDisableButtons(settings);
            },
                    filter: function() {
            this.find('.' + styles.find).removeClass(styles.find);
                    $('.' + styles.noRecordFound).hide();
                    var settings = this.data("jqGridSearchPlugin");
                    var keyItem = $("#" + settings.keyItem);
                    settings._foundItemsArray = new Array();
                    if (keyItem.val() != "" && keyItem.val() != resources.searchText) {
            settings._foundItemsArray = new Array();
                    //Set searchable columns
                    settings._searchInNthColumn = $("#jqGridSearchColumnSelector_" + (jqGridSearchElementCount - 1) + " option:selected").val();
                    if (settings._searchInNthColumn == - 1)
                    selector = this.contents().find("td");
                    else
                    selector = this.find("table tr td:nth-child(" + settings._searchInNthColumn + ")");
                    selector.each(function() {
            var $this = $(this);
                    hideRowByChild($this);
                    if (trToUpper($this.text()).indexOf(trToUpper(keyItem.val())) >= 0)
                    settings._foundItemsArray.push($this);
            });
                    for (var i = 0; i < settings._foundItemsArray.length; i++)
                    showRowByChild(settings._foundItemsArray[i]);
                    enableDisableButtons(settings);
                    if (settings._foundItemsArray.length == 0) {
            //alert(resources.noRecordFound);
            if ($('.' + styles.noRecordFound).length > 0)
                    $('.' + styles.noRecordFound).show();
                    else
                    this.after($("<div />").html(resources.noRecordFound).addClass(styles.noRecordFound));
            }
            }
            else {
            alert(resources.enterCriteria);
                    this.gridSearch('unfilter');
            }



            },
                    unfilter: function() {
            this.find('.' + styles.find).removeClass(styles.find);
                    $('.' + styles.noRecordFound).hide();
                    unhighlightRow(this);
                    this.contents().find("td").each(function() {
            showRowByChild($(this));
            });
                    $("#" + settings.keyItem).val(resources.searchText);
            }
            };
                    initButtons = function(settings) {
            enableDisableButtons(settings);
                    //showOrHideButtons();
            };
                    getColumnHeaders = function(container) {
            var columnHeaders = new Array();
                    //columnHeaders.push("Hepsi");
                    container.contents().find("th").each(function() {
            if ($.trim($(this).text()) != "")
                    ;
                    columnHeaders.push($.trim($(this).text()));
            });
                    return columnHeaders;
            }
            enableDisableButtons = function(settings) {
            b_button = $("#" + settings.buttons.before.selector);
                    n_button = $("#" + settings.buttons.next.selector);
                    f_button = $("#" + settings.buttons.filter.selector);
                    u_button = $("#" + settings.buttons.unfilter.selector);
                    if (settings._foundItemsArray.length > 0) {
            b_button.button({disabled: false});
                    n_button.button({disabled: false});
                    if (settings._foundItemIdx - 1 <= 0)
                    b_button.button({disabled: true});
            }
            else {
            b_button.button({disabled: true});
                    n_button.button({disabled: true});
            }
            };
                    showOrHideButtons = function(settings) {
            b_button = _container.find("." + settings.buttons.before.selector);
                    n_button = _container.find("." + settings.buttons.next.selector);
                    f_button = _container.find("." + settings.buttons.filter.selector);
                    u_button = _container.find("." + settings.buttons.unfilter.selector);
                    if (settings._foundItemsArray.length > 0) {
            if (settings._foundItemsArray.length > 1) {
            b_button.show();
                    n_button.show();
            }
            else {
            b_button.hide();
                    n_button.hide();
            }

            f_button.show();
                    u_button.show();
            }
            else {
            b_button.hide();
                    n_button.hide();
                    f_button.hide();
                    u_button.show();
            }
            };
                    hideRowByChild = function(elm) {
            getRowOfTheElement(elm).hide();
            };
                    showRowByChild = function(elm) {
            getRowOfTheElement(elm).show();
            };
                    unhighlightRow = function(container) {
            //container.find("." + styles.find).removeClass(styles.find);
            container.find("tbody tr").removeClass("ui-state-highlight");
                    container.find("tbody td").removeClass("ui-state-active");
                    //container.find("." + styles.findRow).removeClass(styles.findRow);
            };
                    highlightRow = function(container, item, customScrollHeight) {
            var row = getRowOfTheElement(item);
                    container.scrollTo(container.scrollTop() + row.position().top - row.height() + customScrollHeight, settings.scrollDuration);
                    item.addClass("ui-state-active"); //.addClass(styles.find);
                    getRowOfTheElement(item).addClass("ui-state-highlight"); //.addClass(styles.findRow);
            };
                    wrapText = function(container, start, end) {
            var len = container.text().length;
                    var selectedText = container.text().substring(start, end);
                    var replacement = "<font class='" + styles.findSubstring + "'>" + selectedText + "</font>";
                    container.html(container.text().substring(0, start) + replacement + container.text().substring(end, len));
            };
                    unwrapText = function(container) {
            if (container.text())
                    container.html(container.text().replace("<b>", "").replace("</b>", ""));
            }
            getRowOfTheElement = function(elm) {
            var parentNode = elm.parent();
                    while (parentNode[0].nodeName != "TR") {
            parentNode = parentNode.parent();
            }
            return parentNode;
            };
                    trToUpper = function(str) {
            return str.replace('i', 'İ').replace('ı', 'I').toUpperCase();
            };
                    $.fn.gridSearch = function(method) {
            if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
            }
            else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
            }
            else {
            $.error('Method ' + method + ' does not exist on jQuery.gridSearch');
            }
            };
            })(jQuery);
                    var resources = {
            buttons: {
            before: "Prev",
                    next: "Next",
                    filter: "Filter",
                    unfilter: "Show All"
            },
                    searchText: "Search",
                    endOfSearch: "End of Search!",
                    noRecordFound: "No record found",
                    searchAllColumn: "All",
                    enterCriteria: "Please enter criteria"
            };
                    var styles = {
            searchBar: {
            bottom: "gridViewButtonContainerFooter",
                    top: "gridViewButtonContainerHeader"
            },
                    find: "find",
                    findRow: "findRow",
                    findSubstring: "findSubstring", //ui-state-highlight",
                    noRecordFound: "noRecordFound"
            };
                    $("div").gridSearch({
            primaryAction: "filter",
                    scrollDuration: 0,
                    searchBarAtBottom: false,
                    customScrollHeight: - 35,
                    visible: {
            before: true,
                    next: true,
                    filter: true,
                    unfilter: true
            },
                    textVisible: {
            before: true,
                    next: true,
                    filter: true,
                    unfilter: true
            },
                    minCount: 2
            });
        </script>

    </head>
    <body>
        <h1>Hello World!</h1>
        <div>
            <table>
                <thead>
                    <tr>
                        <th>Fruit</th>
                        <th>Color</th>
                    </tr>
                </thead>

                <tr>
                    <td>Apple</td>
                    <td>Green</td>
                </tr>
                <tr>
                    <td>Grapes</td>
                    <td>Green</td>
                </tr>
                <tr>
                    <td>Orange</td>
                    <td>Orange</td>
                </tr>
            </table>
        </div>
    </body>
</html>
