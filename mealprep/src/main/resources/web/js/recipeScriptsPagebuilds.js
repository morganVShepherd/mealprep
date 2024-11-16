async function fetchIngredientData() {
    try {
        const response = await fetch('http://localhost:8080/get-shopping-list');
        const data = await response.json();

        const tableBody = document.querySelector('#data-table tbody');
        tableBody.innerHTML = ''; // Clear existing data if any

        data.forEach(ingredient => {
            const row = document.createElement('tr');

            row.innerHTML = `

          <td>
             <p>${ingredient.quantity}</p>
          </td>

          <td>
            <p>${ingredient.metric}</p>
          </td>

          <td>
            <p>${ingredient.foodName}</p>
          </td>

`;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

async function getAllRecipes() {
    try {
        const response = await fetch('http://localhost:8080/recipe/all');
        const data = await response.json();

        const tableBody = document.querySelector('#week-data-table tbody');
        tableBody.innerHTML = ''; // Clear existing data if any

        data.forEach(list => {
            const row = document.createElement('tr');

            row.innerHTML = `         

          <td>
            <p>${list.name}</p>
          </td>          
           <td>
            <p>${list.kcal}</p>
          </td>          
          <td>
            <p>${list.servingSize}</p>
          </td>
          <td>
            <p>${list.mealType}</p>
          </td>
           <td>
            <p>${list.notes}</p>
          </td>

          <td>
          <input type="hidden" type="text" name="recpieId" id="recpieId" value="${list.id}">          
          <button type="button"
                            class="btn-block btn btn-success btn-sm"
                            data-toggle="modal"
                            data-target="#recipeModal"
                            data-bs-target=""
                            id="view"
                            onclick="generateRecipeModal(${list.id})">
                        View
          </button>
          </td>

`;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}


async function fetchWeekData() {
    try {
        const response = await fetch('http://localhost:8080/get-weeks-data');
        const data = await response.json();

        const tableBody = document.querySelector('#week-data-table tbody');
        tableBody.innerHTML = ''; // Clear existing data if any

        data.forEach(list => {
            const row = document.createElement('tr');

            row.innerHTML = `

          <td>
             <p>${list.day}</p>
          </td>

          <td>
            <p>${list.name}</p>
          </td>

          <td>
          <input type="hidden" type="text" name="recpieId" id="recpieId" value="${list.id}">          
          <button type="button"
                            class="btn-block btn btn-success btn-sm"
                            data-toggle="modal"
                            data-target="#recipeModal"
                            data-bs-target=""
                            id="view"
                            onclick="generateRecipeModal(${list.id})">
                        View
          </button>
          </td>

`;
            tableBody.appendChild(row);
        });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

async function generateRecipeModal(id) {

    try {
        const response = await fetch('http://localhost:8080/recipe/by-id?id=' + id);
        const data = await response.json();

        const tableBody = document.querySelector('#modalTable tbody');
        tableBody.innerHTML = '';

        data.every(recipe => {
            const row = document.createElement('tr');

            row.innerHTML = `
          <td>
             KCAL ${recipe.kcal} <br/>
             Serving Size ${recipe.servingSize}<br/>
             Rating ${recipe.rating}<br/>
             <div>
              <h4>Ingredients</h4>
             <ul id="ingredientList">
            </div>
            <div>
            <h4>Steps</h4>
             <ul id="stepsList"></ul>
            </div>
                  
          </td> 
            `;
            tableBody.appendChild(row);
            replaceModalListData(recipe.ingredients);
            replaceModalStepsData(recipe.steps);
            replaceModalHeader(recipe.name);

        });
    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

function replaceModalHeader(recipeName){
    var header = document.getElementById("modalHeader");
    header.innerText = recipeName;
}

function replaceModalListData(ingredients){
    var list = document.getElementById("ingredientList");
    for(var i = 0; i < ingredients.length; i++){
        let li = document.createElement('li');
        li.innerText = ingredients[i].quantity + ' '+ ingredients[i].metric + ' '+ ingredients[i].foodTypeDTO.name;
        list.appendChild(li);
    }
}

function replaceModalStepsData(steps){
    var list = document.getElementById("stepsList");
    for(var i = 0; i < steps.length; i++){
        let li = document.createElement('li');
        li.innerText = steps[i].stepNo+'  '+steps[i].details ;
        list.appendChild(li);
    }
}


async function generateWhatsAppModal() {

    try {
        const response = await fetch('http://localhost:8080/get-whastapp-message');
        const data = await response.text();

        const tableBody = document.querySelector('#whatsAppData tbody');
        tableBody.innerHTML = ''; // Clear existing data if any

        const row = document.createElement('tr');
        let td = document.createElement('td');
        td.innerText = data ;

        row.appendChild(td)
        tableBody.appendChild(row);

    } catch (error) {
        console.error('Error fetching data:', error);
    }
}

async function generateMealPrep() {
    try {
        const response = await fetch('http://localhost:8080/prepare-weeks-meals');

    } catch (error) {
        console.error('Error fetching data:', error);
    }
    window.location.reload();
}

function insIngredientRow()
{
    var x=document.getElementById('ingredientsTable');
    // deep clone the targeted row
    var new_row = x.rows[1].cloneNode(true);
    // get the total number of rows
    var len = x.rows.length;
    // set the innerHTML of the first row
    new_row.cells[0].innerHTML = len;

    // grab the input from the first cell and update its ID and value
    var inp1 = new_row.cells[1].getElementsByTagName('input')[0];
    inp1.id += (len);
    inp1.value = '';

    // grab the input from the first cell and update its ID and value
    var inp2 = new_row.cells[2].getElementsByTagName('select')[0];
    inp2.id += (len);
    inp2.value = '';

    var inp3 = new_row.cells[3].getElementsByTagName('input')[0];
    inp3.id += (len);
    inp3.value = '';

    // append the new row to the table
    x.appendChild( new_row );
}

function deleteIngredientRow(row) {
    var i = row.parentNode.parentNode.rowIndex;
    document.getElementById('ingredientsTable').deleteRow(i);
}

function insStepRow() {
    var x = document.getElementById('stepsTable');
    // deep clone the targeted row
    var new_row = x.rows[1].cloneNode(true);
    // get the total number of rows
    var len = x.rows.length;
    // set the innerHTML of the first row
    new_row.cells[0].innerHTML = len;

    // grab the input from the first cell and update its ID and value
    var inp1 = new_row.cells[1].getElementsByTagName('textarea')[0];
    inp1.id += len;
    inp1.value = '';


    x.appendChild(new_row);
}

function deleteStepRow(row) {
    var i = row.parentNode.parentNode.rowIndex;
    document.getElementById('stepsTable').deleteRow(i);
}

async function recipeToJson(){
    await sendJsonFile();
    location.reload();
}

async function sendJsonFile() {

    var ingredientsTable = document.getElementById('ingredientsTable');

    var ingredientsJson =' "ingredients": [\n' +
        '            {\n' +
        '                "quantity": "'+document.getElementById('ingQty').value+'",\n' +
        '                "metric": "'+document.getElementById('ingMetric').value+'",\n' +
        '                "foodTypeDTO": {\n' +
        '                    "name": "'+document.getElementById('ingFood').value+'"\n' +
        '                },\n' +
        '                "ingredientType": "OTHER"\n' +
        '            }\n'

    for (var i = 2; i < ingredientsTable.rows.length; i++) {
        ingredientsJson = ingredientsJson +
            '            ,\n' +
            '            {\n' +
            '                "quantity": "'+document.getElementById('ingQty'+i).value+'",\n' +
            '                "metric": "'+document.getElementById('ingMetric'+i).value+'",\n' +
            '                "foodTypeDTO": {\n' +
            '                    "name": "'+document.getElementById('ingFood'+i).value+'"\n' +
            '                },\n' +
            '                "ingredientType": "OTHER"\n' +
            '            }\n'
    }



    var stepsTable = document.getElementById('stepsTable');
    var stepsJson =' "steps": [\n' +
        '   {\n' +
        '      "stepNo": "1",\n' +
        '      "details": "'+document.getElementById('stpTa').value+'"\n' +
        '   }\n';

    for (var i = 2; i < stepsTable.rows.length; i++) {

        stepsJson = stepsJson +
            '   ,\n' +
            '   {\n' +
            '      "stepNo": "'+i+'",\n' +
            '      "details": "'+document.getElementById('stpTa'+i).value+'"\n' +
            '   }\n';
    }


    var jsonString = '{\n' +
        ingredientsJson+'\n'+
        '],\n'+
        stepsJson+'\n'+
        '],\n'+
        '    "kcal": "'+document.getElementById('kcal').value+'",\n' +
        '        "servingSize": "'+document.getElementById('servingSize').value+'",\n' +
        '        "notes": "Not selected",\n' +
        '        "name": "'+document.getElementById('name').value+'",\n' +
        '        "rating": "'+document.getElementById('rating').value+'",\n' +
        '        "mealType": "DINNER",\n' +
        '        "mealRotation": "OUT"\n' +
        '    }'

    fetch("http://localhost:8080/recipe/createJavaScript", {
        method: "POST",
        body: jsonString,
        headers: {
            "Content-type": "application/json; charset=UTF-8"
        }
    });

    console.log(jsonString);

}