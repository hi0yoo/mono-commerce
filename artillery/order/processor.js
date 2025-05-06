const fs = require('fs');

const products = JSON.parse(fs.readFileSync('./products.json', 'utf8'));

module.exports = {
  generateProductQuantities: function (context, events, done) {
    const numItems = Math.floor(Math.random() * 10) + 1; // 1~10개
    const selected = [];

    for (let i = 0; i < numItems; i++) {
      const p = products[Math.floor(Math.random() * products.length)];
      if (selected.some(e =>
        e.vendorId === p.vendorId &&
        e.productId === p.productId &&
        e.optionId === p.optionId
      )) continue;

      selected.push({
        vendorId: p.vendorId,
        productId: p.productId,
        optionId: p.optionId,
        quantity: Math.floor(Math.random() * 5) + 1, // 수량 1~5
      });
    }

    context.vars.productQuantities = selected;
    return done();
  }
};