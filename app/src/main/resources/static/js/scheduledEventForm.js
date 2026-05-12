var containerSeats = document.querySelector(".container-seats");

let selectedSeats = [];
var containerSelectedSeats = document.querySelector(".container-selected-seats");
var selectArea = document.querySelector("select[name='arenaArea']");

setSeats(containerSeats);

function setSeats(containerSeats) {
  /*
   * ===========
   * ADD NUMBERS
   * ===========
   */

  var numbers = document.createElement("div");
  numbers.classList.add("numbers");

  for (let index = 1; index <= 6; index++) {
    var div = document.createElement("div");

    div.classList.add("number");
    div.classList.add("element");

    var span = document.createElement("span");

    span.innerText = index;

    div.appendChild(span);
    numbers.appendChild(div);
  }

  containerSeats.appendChild(numbers);

  /*
   * =========
   * ADD SEATS
   * =========
   */

  var seats = document.createElement("div");
  seats.classList.add("seats");
  var alfa = "ABCDEF"
  for (let letter = 1; letter <= 6; letter++){
    for (let index = 1; index <= 6; index++) {
      let div = document.createElement("div");
      
      div.classList.add("seat");
      div.classList.add("element");
      div.classList.add("available");
      
      div.onclick = function () {toggleSelectedSeat(alfa[letter - 1] + index); div.classList.toggle("selected")};
      
      var i = document.createElement("i");
      i.classList.add("fa-solid");
      i.classList.add("fa-couch");
      
      div.appendChild(i);
      seats.appendChild(div);
    }
  }

  containerSeats.appendChild(seats);

  /*
   * ===========
   * ADD LETTERS
   * ===========
   */

  var letters = document.createElement("div");
  letters.classList.add("letters");

  for (let index = 0; index < 6; index++) {
    var alfa = "ABCDEF"
    var div = document.createElement("div");

    div.classList.add("letter");
    div.classList.add("element");

    var span = document.createElement("span");

    span.innerText = alfa[index];

    div.appendChild(span);
    letters.appendChild(div);
  }

  containerSeats.appendChild(letters);
}

function toggleSelectedSeat(seatNumber = 0){
  if(selectedSeats.includes(seatNumber)){
    let index = selectedSeats.indexOf(seatNumber);
    if(index > -1){
      selectedSeats.splice(index, 1);
    }
  }
  else{
    selectedSeats.push(seatNumber);
  }

  if(selectedSeats.length > 0){
    containerSelectedSeats.innerText = "Assentos selecionados: " + selectedSeats.toString();
  }
  else{
    containerSelectedSeats.innerText = "* Não há assentos selecionados.";
  }

}