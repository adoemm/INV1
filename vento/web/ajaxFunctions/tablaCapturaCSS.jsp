<%@page contentType="text/html;charset=utf-8" pageEncoding="utf-8" language="java"%>

<style type="text/css">
    .cssLayout {
        margin:0px;padding:0px;
        width:100%;                
        border:1px solid #cccccc;

        -moz-border-radius-bottomleft:8px;
        -webkit-border-bottom-left-radius:8px;
        border-bottom-left-radius:8px;

        -moz-border-radius-bottomright:8px;
        -webkit-border-bottom-right-radius:8px;
        border-bottom-right-radius:8px;

        -moz-border-radius-topright:8px;
        -webkit-border-top-right-radius:8px;
        border-top-right-radius:8px;

        -moz-border-radius-topleft:8px;
        -webkit-border-top-left-radius:8px;
        border-top-left-radius:8px;
    }.cssLayout table{
        width:100%;
        height:100%;
        margin:0px;padding:0px;
    }.cssLayout tr:last-child td:last-child {
        -moz-border-radius-bottomright:8px;
        -webkit-border-bottom-right-radius:8px;
        border-bottom-right-radius:8px;
    }
    .cssLayout table tr:first-child td:first-child {
        -moz-border-radius-topleft:8px;
        -webkit-border-top-left-radius:8px;
        border-top-left-radius:8px;

    }
    .cssLayout table tr:first-child td:last-child {
        -moz-border-radius-topright:8px;
        -webkit-border-top-right-radius:8px;
        border-top-right-radius:8px;
    }.cssLayout tr:last-child td:first-child{
        -moz-border-radius-bottomleft:8px;
        -webkit-border-bottom-left-radius:8px;
        border-bottom-left-radius:8px;
    }.cssLayout tr:hover td{

    }
    .cssLayout tr:nth-child(odd){ background-color:#d9efb4; }
    .cssLayout tr:nth-child(even)    { background-color:#ffffff; }


    .cssLayout td{
        vertical-align:middle;
        border:1px solid #cccccc;
        border-width:0px 1px 1px 0px;
        text-align:left;
        padding:3px;
        font-size:12px;
        font-family:Arial;
        font-weight:normal;
        color:#000000;
        height:12px;

    }.cssLayout tr:last-child td{
        border-width:0px 1px 0px 0px;
    }.cssLayout tr td:last-child{
        border-width:0px 0px 1px 0px;
    }.cssLayout tr:last-child td:last-child{
        border-width:0px 0px 0px 0px;
    }
    .cssLayout tr:first-child td{
        background:-o-linear-gradient(bottom, #ededed 5%, #ffffff 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #ededed), color-stop(1, #ffffff) );
        background:-moz-linear-gradient( center top, #ededed 5%, #ffffff 100% );
        filter:progid:DXImageTransform.Microsoft.gradient(startColorstr="#ededed", endColorstr="#ffffff");	background: -o-linear-gradient(top,#ededed,ffffff);
        background-color:#ededed;
        border:0px solid #cccccc;
        text-align:center;
        border-width:0px 0px 1px 1px;
        font-size:14px;
        font-family:Arial;
        font-weight:bold;
        color:#000000;
    }
    .cssLayout tr:first-child:hover td{
        background:-o-linear-gradient(bottom, #E9E9E9 5%, #ffffff 100%);	background:-webkit-gradient( linear, left top, left bottom, color-stop(0.05, #E9E9E9), color-stop(1, #ffffff) );
        background:-moz-linear-gradient( center top, #E9E9E9 5%, #ffffff 100% );
        filter:progid:DXImageTransform.Microsoft.gradient(startColorstr="##E9E9E9", endColorstr="#ffffff");	background: -o-linear-gradient(top,#E9E9E9,ffffff);
        background-color:#ededed;
    }
    .cssLayout tr:first-child td:first-child{
        border-width:0px 0px 1px 0px;
    }
    .cssLayout tr:first-child td:last-child{
        border-width:0px 0px 1px 1px;
    }

    .cssLayout  td:last-child {
        text-align:center;                               
        width: 15%;
    }
    .cssLayout tr td:first-child{ 
        text-align:center;
    }
    .cssLayout td:nth-child(2){
        text-align:center;            }
    .cssLayout tbody tr:hover {
        background:#BCDC83;
        /*Efecto desvanecido*/
        /* -o-transition: all 0.2s ease-in-out;
        -webkit-transition: all 0.2s ease-in-out;
        -moz-transition: all 0.2s ease-in-out;
        -ms-transition: all 0.2s ease-in-out;
        transition: all 0.2s ease-in-out;*/
    }
    .selectP {
        padding:0px;
        margin: 0;
        cursor:pointer;
        /*Estilizado*/
        -webkit-border-radius:4px;
        -moz-border-radius:4px;
        border-radius:4px;
        /*            -webkit-box-shadow: 0 3px 0 #ccc, 0 -1px #fff inset;
                    -moz-box-shadow: 0 3px 0 #ccc, 0 -1px #fff inset;
                    box-shadow: 0 3px 0 #d9efb4, 0 -1px #fff inset;*/
        background: #FFFFFF;
        color:#000000;
        border:solid;
        border-width: 1px;
        border-color: #666666;
        outline:none;
        display: inline-block;
        -webkit-appearance:none;
        -moz-appearance:none;
        appearance:none;
        width: 60px;
    }

</style>
