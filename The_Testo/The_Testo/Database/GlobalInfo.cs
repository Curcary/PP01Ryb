using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace The_Testo.Database
{
    static class GlobalInfo
    {
        public static User CurrentUser { get; set; }
        public static Dish SelectedDish { get; set; }
    }
}
