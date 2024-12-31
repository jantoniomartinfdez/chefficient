Description of the project: Chefficient is designed with the intent of smartly matching recipes to food and vice-versa, with the goal of reducing food waste and save money.

Actors:
	- Chef / Administrator: individual who is trained to create recipes from scratch with fresh ingredients.
	- Cook / User: individual who follows established recipes to prepare food and perform food logistics.

List of main requirements:
	- [M.V.P.] Search recipes based on selected ingredients, order by the ones that contain more matches, in descendant order.
	- [M.V.P.] List ingredients needed for a selected recipe.
	- Show list of possible recipes based on inventory, order by the ones that can be fully done with existing ingredients, in descendant order.
	- List ingredients needed for a selected group of recipes (food menu), previously added.
	- Show summary menu, which involves total ingredients, missing ones, total recipes, total nutrition list, etc.
	- List missing ingredients on inventory needed for a selected recipe, in order to buy them.
	
	
Glossary of terms:
	- Recipe: a set of ingredients, instructions and optionally recommendations in order to elaborate a dish. It also contains a title, sometimes followed by a description to be easily identified.
	- Recipe group: same as "menu", with fixed amount of recipes based on frequency (daily, weekly or monthly).
	- Ingredient: a certain amount of food used as part of a recipe.
	- Inventory: same as "pantry", a.k.a "Food Repository" here.
	- Food: standalone and concrete edible item, used for one or many ingredients.
	
Steps for launching the app:
	- docker compose up -d
	- mvn clean package
	- docker build -t javalin-app .
	- docker run --network=host javalin-app
	- Go to http://localhost:7070/