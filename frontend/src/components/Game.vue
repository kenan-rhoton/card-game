<template>
    <div id="game">
        <div v-if="response.status != 'Waiting for an opponent'"
            id="board"
            class="row">
            <div id="my-score" class="col text-right">0</div>
            <div id="opponent-score" class="col text-left">0</div>
            <div class="w-100"></div>
            <div id="rows" class="col-12">
                <div v-for="(row, rownum) in response.rows"
                    :key="row.$index"
                    class="game-row row border"
                    style="height: 50px"
                    :rownum="rownum"
                    v-on:dragover="allowDrop"
                    v-on:drop="dropOnRow">
                    <div v-for="card in row"
                        :key="card.$index"
                        :rownum="rownum"
                        v-on:dragover="allowDrop"
                        v-on:drop="dropOnRow"
                        class="card col-1">
                        {{card.power}}
                        {{card.owner | cut}}
                    </div>
                </div>
            </div>
            <div class="col-12">
                <div class="hand row">
                    <div v-for="(card, index) in response.hand"
                        :key="index"
                        class="card col"
                        :index="index"
                        draggable
                        v-on:dragstart="dragCardFromHand">
                        {{card.power}}
                    </div>
                </div>
            </div>
        </div>
        <div v-else id="waiting-for-opponent" class="row">
            <div id="game-status">
                Waiting for opponent
            </div>
            <div v-if="joinLink" id="join-link">{{joinLink}}</div>
        </div>
    </div>
</template>

<script>
import axios from "axios";
export default {
  name: "Game",
  data: function() {
    var defaultData = {};
    defaultData.response = {};
    return defaultData;
  },
  methods: {
    gameReq: function() {
      return (
        "http://backend:3000/games/" +
        this.$route.params.gameID +
        "/player/" +
        this.$route.params.playerID
      );
    },
    updateGame: async function() {
      try {
        const req = this.gameReq();
        const response = await axios.get(req);
        this.response = response.data;
      } catch (err) {
        throw err;
      }
    },
    dragCardFromHand: function(ev) {
      ev.dataTransfer.setData("handIndex", ev.target.getAttribute("index"));
    },
    allowDrop: function(ev) {
      ev.preventDefault();
    },
    dropOnRow: async function(ev) {
      ev.preventDefault();
      try {
        const req = this.gameReq();
        const data = ev.dataTransfer.getData("handIndex");
        const sentData = { index: data, row: ev.target.getAttribute("rownum") };
        console.log(ev.target);
        console.log(sentData);
        const response = await axios.post(req, sentData);
        console.log(response);
      } catch (err) {
        throw err;
      }
    }
  },
  computed: {
    joinLink: function() {
      const loc = window.location;
      return (
        loc.protocol +
        "//" +
        loc.hostname +
        ":" +
        loc.port +
        "/#/join-game/" +
        this.$route.params.gameID
      );
    }
  },
  created: function() {
    this.updateGame();
    setInterval(
      function() {
        this.updateGame();
      }.bind(this),
      1000
    );
  },
  filters: {
    cut: function(word) {
      return word.slice(0, 3);
    }
  }
};
</script>
