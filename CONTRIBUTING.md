# Project architecture

The project is divided (as of now) into two main sections: *backend* and *frontend*, each with their quirks.

## Backend architecture

We use [Clojure](https://www.braveclojure.com) as the backend language for pretty much **everything** due to basically two reasons:

1. Because of the "everything is immutable" paradigm, it's very easy to reason about the logic of the code, which will probably get very complex as time goes on.
2. Due to its functional nature, it is *very* easy to run tests on it and a strict [TDD](https://www.youtube.com/watch?v=qkblc5WRn-U) discipline.

The backend also uses [Redis](https://redis.io) as a key-value storage mechanism for any persistent information necessary (such as the state of a game) since we will *always* know exactly what key we're searching for (we'll know the game ID, player ID, etc.).

Finally, the backend uses [Compojure](https://github.com/weavejester/compojure) to define it's [REST API](http://www.restapitutorial.com), which is the way the *frontend* will communicate with it.

## Frontend architecture

Frontend uses a standard [Vue.js](https://vuejs.org) architecture due to being easy to set up and not overcomplicating the frontend aspects.

## Docker

We also use [Docker](https://docs.docker.com) to launch everything. Why?

1. Dependency management: Docker creates a *container* (think of it as a mini-virtual-machine) that is built according to our specified `Dockerfile` and as such contains all of our projects dependencies without you having to set anything up (other than installing Docker)
2. Stack Identity: As a consequence of this, the development and production stack (the set of technologies used) is guaranteed to be the same everywhere. Since Docker will launch with the same build, dependencies and options on each of our systems, we should never have *"but it works on my computer!"* problems.
3. Isolated: Since our programs run inside a Docker container, which only has the bare minimum to make our programs run, there is less chance of some external tool or program clashing with our program's execution or viceversa.

And how does it work?

Learn this and much more by [RTFM](https://docs.docker.com/engine/docker-overview/)! :D

# Project structure

The structure of a project (where the f*** is that file?) might be more important than it's architecture. In this case, there is a three-way split: *backend* and *frontend* as before, but we also have de *e2e* tests to worry about.

But some things also happen at the root:

- **docker-compose.yml**: Defines how the different parts of the project launch and interact with each other.
- **.travis.yml**: Defines how the different pars of the project are to be tested.

However, we will understand this further in the following sections.

## Backend structure

- **backend/Dockerfile**: Defines how the Docker image for the backend will be built and run
- **backend/project.clj**: Defines the project dependencies and entry point
- **backend/test/card_game/**: Contains all the unit tests for the code. Remember that a unit test must be done *before* the actual code.
- **backend/src/card_game/**: Contains the code that will run in production, the actual logic.
- **backend/src/card_game/persistence.clj**: This is the only file to interact directly with the database. All database calls *must* pass through here.
- **backend/src/card_game/api_handler.clj**: This is the only file to interact directly with frontend. Any actions to take as a response to a frontend call *must* be defined here.

## Frontend structure

- **frontend/Dockerfile** and **frontend/entry.sh**: Defines the Docker image and its entrypoint.
- **frontend/package.json** and **frontend/yarn.lock**: Define the project dependencies (`yarn.lock` is autogenerated by `yarn`)
- **frontend/test/unit/**: Contains all the unit tests for the code. Remember that a unit test must be done *before* the actual code.
- **frontend/public/**: The "website", with `index.html` being the entrypoint, but the real magic happens elsewhere...
- **frontend/src/**: Contains the code that will run in production, with its own structure.
- **frontend/src/main.js**: The official entrypoint for Vue. Should only be modified if we add a dependency that works through `Vue.use(dependency)`
- **frontend/src/router.js**: Here the "routes" are defined, which is the way Vue interprets what component or view to serve based on the URL (for example `http://frontend:8080/#/create-game` points to the CreateGame component)
- **frontend/src/App.vue**: The main Vue component. Since we're mostly using everything through `router` this mainly hosts the `<router-view/>` (which renders the component the `router` has decided upon based on our URL) and little else.
- **frontend/src/components/** and **frontend/src/views**: The application logic, separated into "views" and "components" following no good criteria as of yet, we will improve on this in the future.


# Contributing guidelines

For any change or contribution, this is what is expected of you:

1. Announce within an issue that you want it assigned to you (or open said issue yourself).
2. Do not take on more than one issue at a time
3. Design the tests for your issue (unit tests always and in some cases e2e tests may be required)
4. Make them pass
5. Ask for a review from a collaborator
6. If changes are requested or travis does not pass, go to 3.

We will value the following things on your contribution *in this order*:

1. The tests pass
2. The code has tests
3. The tests are well-designed
4. The code is readable
5. The code is well-organized
6. The code works and fixes the issue

If at any point you have a question, please ask it if possible through a comment so others may benefit from the answer.

# Other notes

1. We use a strict [TDD](http://www.javiersaldana.com/tech/2014/11/26/refactoring-the-three-laws-of-tdd.html) discipline to contribute to this project, and we expect at *least* that level of tests from any contribution. If we believe the tests don't cover enough production cases, you will be asked to review your code.
2. We always merge through `Squash & Merge`, which will cause you conflicts if you make a pull request from your personal fork's  master branch. We will warn you about this if we see it, as we think it's much more convenient to use your master branch *purely* to be up to date with ours and to make a branch for each feature.