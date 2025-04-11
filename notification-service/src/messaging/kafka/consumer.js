const { Kafka, logLevel } = require("kafkajs");
const {
	kafka: { brokers, clientId, groupId, topic },
} = require("../../configs/app.settings");
const { saveMessage } = require("../../services/notificationService");
console.log("KK Port: ", brokers);
const kafka = new Kafka({
	logLevel: logLevel.INFO,
	clientId: clientId,
	brokers: brokers,
});

const consumer = kafka.consumer({ groupId: groupId });

const runConsumer = async () => {
	console.log("Starting consumer...");

	await consumer.connect();
	await consumer.subscribe({ topic: topic, fromBeginning: true });

	await consumer.run({
		eachMessage: async ({ topic, partition, message }) => {
			const { content, sender, receiver  } = JSON.parse(message.value.toString());
			// console.log("Received message: ", JSON.parse(message.value.toString()))

			await saveMessage({
				receiver: receiver,
				sender: sender,
				content: content,
			});
		},
	});
};

module.exports = {
	runConsumer,
};
