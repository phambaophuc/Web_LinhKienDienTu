var menuHolder = document.getElementById('menuHolder')
var siteBrand = document.getElementById('siteBrand')
function menuToggle(){
  if(menuHolder.className === "drawMenu") menuHolder.className = ""
  else menuHolder.className = "drawMenu"
}
if(window.innerWidth < 426) siteBrand.innerHTML = "MAS"
window.onresize = function(){
  if(window.innerWidth < 420) siteBrand.innerHTML = "MAS"
  else siteBrand.innerHTML = "MY AWESOME WEBSITE"
}

// trạng thái click vào thanh search
document.addEventListener("DOMContentLoaded", function() {
    const searchbar = document.querySelector(".searchbar");
    const searchInput = document.querySelector(".search_input");

    searchbar.addEventListener("click", function() {
        searchbar.classList.add("active");
        searchInput.focus();
    });

    document.addEventListener("click", function(e) {
        const isSearchbarClicked = searchbar.contains(e.target);
        if (!isSearchbarClicked) {
            searchbar.classList.remove("active");
        }
    });
});