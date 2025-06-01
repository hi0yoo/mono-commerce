const fs = require('fs')

const products = JSON.parse(fs.readFileSync('./products.json', 'utf8'))

module.exports = {
  generateProductQuantities: function (context, events, done) {
    const numItems = Math.floor(Math.random() * 10) + 1 // 1~10개
    const picked = new Set()
    while (picked.size < numItems) {
      const candidate = products[Math.floor(Math.random() * products.length)]
      picked.add(candidate)
    }

    context.vars.productQuantities = Array.from(picked).map(id => ({
      productOptionId: id,
      quantity: Math.floor(Math.random() * 5) + 1,
    }))

    return done()
  }
}