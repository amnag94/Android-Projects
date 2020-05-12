/*let ingredients = {
    "dal": { 'id' : 1, 'measure' : "cups", 'quantity' : 2 },
    "rice": { 'id' : 2, 'measure' : "cups", 'quantity' : 2 }
};

module.exports = {

    get_ingredients : function() {
        return ingredients;
    },

    get_ingredient : function(ingredient_name) {
        return ingredients[ingredient_name];
    },
}*/

class Ingredient {
    constructor(id, measure, quantity) {
        this.id = id;
        this.measure = measure;
        this.quantity = quantity;
    };
}

class Ingredients {

    constructor() {
        this.ingredients = {
            
        };
    }

    get_ingredients() {
        return this.ingredients;
    }

    get_ingredient(ingredient_name) {
        return this.ingredients[ingredient_name];
    }

    get_id() {
        return Object.keys(this.ingredients).length + 1;
    }

    add_ingredients(ingredient_name, measure, quantity) {
        var id;
        var return_string;

        if (ingredient_name in this.ingredients) {
            id = this.ingredients[ingredient_name].id;
            return_string =`Updated`;
        }
        else {
            id = this.get_id();
            return_string = `Added`;
        }

        this.ingredients[ingredient_name] = new Ingredient(id, measure, parseFloat(quantity));
        return return_string;
    }
}

module.exports.Ingredients = Ingredients;