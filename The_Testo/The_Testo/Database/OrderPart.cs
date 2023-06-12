using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace The_Testo.Database
{
    partial class Order
    {
        public float Sum { get
            {
                float Sum = 0;
                Sum = (float)(from od in DB.entities.Ordered_dishes
                                   from d in DB.entities.Dish
                                   where od.Order.OrderID == OrderID && od.DishID == d.DishID
                                   select d.DishPrice*od.DishAmount).ToList().Sum();

                return Sum;
            } }
    }
}
