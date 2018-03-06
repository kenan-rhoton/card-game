"use strict";

define(function (require) {
    return {
        buildRows() {
            const baseRow = document.getElementById("row-template")
                .content.querySelector("div");
            const myRows = document.getElementById("my-rows");
            const oppRows = document.getElementById("opp-rows");

            for (var i = 0; i < 5; i++) {
                var myNewRow = baseRow.cloneNode(true);
                myNewRow.setAttribute("rownum", i);

                myRows.appendChild(myNewRow);

                var oppRow = baseRow.cloneNode(true);
                oppRows.appendChild(oppRow);
            }
        }
    }
});
