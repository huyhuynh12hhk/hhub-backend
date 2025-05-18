const { Kafka, logLevel } = require("kafkajs");
const {
	kafka: { brokers, clientId, groupId, topic },
} = require("../../configs/appSettings");
const { saveMessage } = require("../../services/NotificationService");
//console.log("KK Port: ", brokers);
const kafka = new Kafka({
	logLevel: logLevel.INFO,
	clientId: clientId,
	brokers: brokers,
});
const admin = kafka.admin();
const consumer = kafka.consumer({ groupId: groupId });

const ensureTopicExists = async () => {
	try {
		await admin.connect();
		const topicList = await admin.listTopics();

		if (!topicList.includes(topic)) {
			// If topic does not exist, create it
			await admin.createTopics({
				topics: [
					{
						topic: topic,
						numPartitions: 3,
						replicationFactor: 1,
					},
				],
			});
			console.log(`Topic "${topic}" created successfully.`);
		} else {
			console.log(`Start listen from topic "${topic}".`);
		}
	} catch (error) {
		console.error("Error ensuring topic exists:", error);
	} finally {
		await admin.disconnect();
	}
};

const runConsumer = async () => {
	console.log("Starting consumer...");

	await consumer.connect();
	await consumer.subscribe({ topic: topic, fromBeginning: true });

	await consumer.run({
		eachMessage: async ({ topic, partition, message }) => {
			const { content, sender, receiver } = JSON.parse(message.value.toString());
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
	ensureTopicExists
};
