import { expect } from "chai";
import { shallow } from "@vue/test-utils";
import JoinGame from "@/components/JoinGame.vue";

describe("JoinGame", () => {
  it("renders", () => {
    var pushParams = {};
    const $route = { params: { joinID: 5 } };
    const $router = { push: a => (pushParams = a) };
    shallow(JoinGame, {
      mocks: { $route, $router },
      methods: {
        fetchGame: () => {
          return { data: { "player-id": 3 } };
        }
      }
    });
    expect(pushParams).to.eql({
      name: "Game",
      params: {
        gameID: 5,
        playerID: 3
      }
    });
  });
});
